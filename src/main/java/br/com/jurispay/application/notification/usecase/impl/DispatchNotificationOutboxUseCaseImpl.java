package br.com.jurispay.application.notification.usecase.impl;

import br.com.jurispay.application.notification.port.WhatsAppSenderPort;
import br.com.jurispay.application.notification.usecase.DispatchNotificationOutboxUseCase;
import br.com.jurispay.domain.notification.model.NotificationOutboxStatus;
import br.com.jurispay.infrastructure.notification.persistence.jpa.repository.SpringDataNotificationOutboxRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class DispatchNotificationOutboxUseCaseImpl implements DispatchNotificationOutboxUseCase {

    private final SpringDataNotificationOutboxRepository outboxRepository;
    private final WhatsAppSenderPort whatsAppSenderPort;

    public DispatchNotificationOutboxUseCaseImpl(
            SpringDataNotificationOutboxRepository outboxRepository,
            WhatsAppSenderPort whatsAppSenderPort
    ) {
        this.outboxRepository = outboxRepository;
        this.whatsAppSenderPort = whatsAppSenderPort;
    }

    @Override
    public int dispatch() {
        var now = Instant.now();
        var candidates = outboxRepository.findDispatchCandidates(now);
        int processed = 0;

        for (var o : candidates) {
            if (!"WHATSAPP".equalsIgnoreCase(o.getChannel())) {
                markFailed(o, "Canal não suportado: " + o.getChannel());
                processed++;
                continue;
            }

            try {
                var result = whatsAppSenderPort.sendText(o.getToPhone(), o.getRenderedMessage());
                o.setStatus(NotificationOutboxStatus.SENT.name());
                o.setProviderMessageId(result.providerMessageId());
                o.setSentAt(Instant.now());
                o.setLastError(null);
                o.setNextAttemptAt(null);
                outboxRepository.save(o);
                processed++;
            } catch (Exception ex) {
                scheduleRetryOrFail(o, ex);
                processed++;
            }
        }

        return processed;
    }

    private void scheduleRetryOrFail(br.com.jurispay.infrastructure.notification.persistence.jpa.entity.NotificationOutboxEntity o, Exception ex) {
        int attempts = o.getAttempts() + 1;
        o.setAttempts(attempts);
        o.setLastError(truncate(ex.getMessage()));

        if (attempts >= 6) {
            o.setStatus(NotificationOutboxStatus.FAILED.name());
            o.setNextAttemptAt(null);
            outboxRepository.save(o);
            return;
        }

        o.setStatus(NotificationOutboxStatus.RETRY.name());
        o.setNextAttemptAt(Instant.now().plus(backoff(attempts)));
        outboxRepository.save(o);
    }

    private Duration backoff(int attempt) {
        return switch (attempt) {
            case 1 -> Duration.ofMinutes(1);
            case 2 -> Duration.ofMinutes(5);
            case 3 -> Duration.ofMinutes(15);
            case 4 -> Duration.ofHours(1);
            case 5 -> Duration.ofHours(6);
            default -> Duration.ofHours(24);
        };
    }

    private void markFailed(br.com.jurispay.infrastructure.notification.persistence.jpa.entity.NotificationOutboxEntity o, String error) {
        o.setStatus(NotificationOutboxStatus.FAILED.name());
        o.setLastError(truncate(error));
        o.setNextAttemptAt(null);
        outboxRepository.save(o);
    }

    private String truncate(String value) {
        if (value == null) {
            return null;
        }
        String v = value.trim();
        if (v.length() <= 1000) {
            return v;
        }
        return v.substring(0, 1000);
    }
}
