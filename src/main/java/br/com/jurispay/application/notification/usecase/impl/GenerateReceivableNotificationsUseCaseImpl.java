package br.com.jurispay.application.notification.usecase.impl;

import br.com.jurispay.application.notification.port.ReceivableNotificationCandidate;
import br.com.jurispay.application.notification.port.ReceivableNotificationQueryPort;
import br.com.jurispay.application.notification.service.NotificationDedupKeyFactory;
import br.com.jurispay.application.notification.service.NotificationPhoneResolver;
import br.com.jurispay.application.notification.service.NotificationTemplateRenderer;
import br.com.jurispay.application.notification.usecase.GenerateReceivableNotificationsUseCase;
import br.com.jurispay.domain.notification.model.NotificationChannel;
import br.com.jurispay.domain.notification.model.NotificationOutboxStatus;
import br.com.jurispay.domain.notification.model.NotificationType;
import br.com.jurispay.domain.systemconfig.repository.SystemConfigRepository;
import br.com.jurispay.infrastructure.notification.persistence.jpa.entity.NotificationOutboxEntity;
import br.com.jurispay.infrastructure.notification.persistence.jpa.repository.SpringDataNotificationOutboxRepository;
import br.com.jurispay.infrastructure.notification.persistence.jpa.repository.SpringDataNotificationTemplateRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

@Service
public class GenerateReceivableNotificationsUseCaseImpl implements GenerateReceivableNotificationsUseCase {

    private static final long DEFAULT_SYSTEM_CONFIG_ID = 1L;

    private final SystemConfigRepository systemConfigRepository;
    private final ReceivableNotificationQueryPort receivableQueryPort;
    private final SpringDataNotificationTemplateRepository templateRepository;
    private final SpringDataNotificationOutboxRepository outboxRepository;
    private final NotificationTemplateRenderer templateRenderer;
    private final NotificationPhoneResolver phoneResolver;

    public GenerateReceivableNotificationsUseCaseImpl(
            SystemConfigRepository systemConfigRepository,
            ReceivableNotificationQueryPort receivableQueryPort,
            SpringDataNotificationTemplateRepository templateRepository,
            SpringDataNotificationOutboxRepository outboxRepository,
            NotificationTemplateRenderer templateRenderer,
            NotificationPhoneResolver phoneResolver
    ) {
        this.systemConfigRepository = systemConfigRepository;
        this.receivableQueryPort = receivableQueryPort;
        this.templateRepository = templateRepository;
        this.outboxRepository = outboxRepository;
        this.templateRenderer = templateRenderer;
        this.phoneResolver = phoneResolver;
    }

    @Override
    public int generateReminders() {
        var config = systemConfigRepository.findById(DEFAULT_SYSTEM_CONFIG_ID)
                .orElseThrow(() -> new IllegalStateException("SystemConfig não encontrado."));

        ZoneId zoneId = ZoneId.of(config.getNotificationTimezone());
        LocalDate today = LocalDate.now(zoneId);

        int created = 0;
        created += generateForType(NotificationType.RECEIVABLE_D1, today, zoneId, config.getPixKey());
        created += generateForType(NotificationType.RECEIVABLE_D0, today, zoneId, config.getPixKey());
        return created;
    }

    @Override
    public int generateCollections() {
        var config = systemConfigRepository.findById(DEFAULT_SYSTEM_CONFIG_ID)
                .orElseThrow(() -> new IllegalStateException("SystemConfig não encontrado."));

        ZoneId zoneId = ZoneId.of(config.getNotificationTimezone());
        LocalDate today = LocalDate.now(zoneId);

        return generateOverdue(today, zoneId, config.getPixKey());
    }

    private int generateForType(NotificationType type, LocalDate today, ZoneId zoneId, String pixKey) {
        LocalDate targetDate = switch (type) {
            case RECEIVABLE_D1 -> today.plusDays(1);
            case RECEIVABLE_D0 -> today;
            default -> throw new IllegalArgumentException("Tipo não suportado: " + type);
        };

        Instant start = targetDate.atStartOfDay(zoneId).toInstant();
        Instant end = targetDate.plusDays(1).atStartOfDay(zoneId).toInstant();

        var candidates = receivableQueryPort.findPendingDueBetween(start, end);
        int created = 0;
        for (var c : candidates) {
            if (createOutboxIfAbsent(type, c, today, zoneId, pixKey)) {
                created++;
            }
        }
        return created;
    }

    private int generateOverdue(LocalDate today, ZoneId zoneId, String pixKey) {
        Instant endExclusive = today.atStartOfDay(zoneId).toInstant();
        var candidates = receivableQueryPort.findPendingOverdueBefore(endExclusive);
        int created = 0;
        for (var c : candidates) {
            if (createOutboxIfAbsent(NotificationType.RECEIVABLE_OVERDUE, c, today, zoneId, pixKey)) {
                created++;
            }
        }
        return created;
    }

    private boolean createOutboxIfAbsent(NotificationType type,
                                        ReceivableNotificationCandidate candidate,
                                        LocalDate referenceDate,
                                        ZoneId zoneId,
                                        String pixKey) {
        String dedupKey = NotificationDedupKeyFactory.forReceivable(type, candidate.receivableId(), referenceDate);
        if (outboxRepository.existsByDedupKey(dedupKey)) {
            return false;
        }

        String toPhone = phoneResolver.resolveToPhone(candidate.customerCellphone(), candidate.customerPhone());
        if (toPhone == null) {
            return false;
        }

        String templateCode = type.name();
        var template = templateRepository.findByCodeAndActiveIsTrue(templateCode)
                .orElse(null);
        if (template == null) {
            return false;
        }

        LocalDate dueLocalDate = candidate.dueDate().atZone(zoneId).toLocalDate();
        int daysOverdue = (int) (referenceDate.toEpochDay() - dueLocalDate.toEpochDay());
        if (daysOverdue < 0) {
            daysOverdue = 0;
        }

        String rendered = templateRenderer.render(template.getContent(), buildValues(candidate, dueLocalDate, daysOverdue, pixKey));

        var entity = NotificationOutboxEntity.builder()
                .dedupKey(dedupKey)
                .type(type.name())
                .channel(NotificationChannel.WHATSAPP.name())
                .status(NotificationOutboxStatus.PENDING.name())
                .customerId(candidate.customerId())
                .loanId(candidate.loanId())
                .receivableId(candidate.receivableId())
                .toPhone(toPhone)
                .templateCode(templateCode)
                .renderedMessage(rendered)
                .attempts(0)
                .nextAttemptAt(null)
                .lastError(null)
                .sentAt(null)
                .providerMessageId(null)
                .build();

        try {
            outboxRepository.save(entity);
            return true;
        } catch (DataIntegrityViolationException ex) {
            return false;
        }
    }

    private Map<String, String> buildValues(ReceivableNotificationCandidate c,
                                           LocalDate dueLocalDate,
                                           int daysOverdue,
                                           String pixKey) {
        Locale locale = new Locale("pt", "BR");
        NumberFormat currency = NumberFormat.getNumberInstance(locale);
        currency.setMinimumFractionDigits(2);
        currency.setMaximumFractionDigits(2);

        String amount = c.amount() == null
                ? ""
                : currency.format(c.amount().setScale(2, RoundingMode.HALF_UP));

        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return Map.of(
                "customer_name", safe(c.customerName()),
                "due_date", dueLocalDate.format(dateFmt),
                "amount", amount,
                "pix_key", safe(pixKey),
                "installment_number", c.installmentNumber() == null ? "" : String.valueOf(c.installmentNumber()),
                "total_installments", c.totalInstallments() == null ? "" : String.valueOf(c.totalInstallments()),
                "days_overdue", String.valueOf(daysOverdue)
        );
    }

    private String safe(String value) {
        if (value == null) {
            return "";
        }
        String v = value.trim();
        return v.isBlank() ? "" : v;
    }
}
