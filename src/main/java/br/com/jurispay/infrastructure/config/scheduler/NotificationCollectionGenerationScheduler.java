package br.com.jurispay.infrastructure.config.scheduler;

import br.com.jurispay.application.notification.usecase.GenerateReceivableNotificationsUseCase;
import br.com.jurispay.domain.systemconfig.repository.SystemConfigRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class NotificationCollectionGenerationScheduler {

    private static final Long DEFAULT_SYSTEM_CONFIG_ID = 1L;

    private final SystemConfigRepository systemConfigRepository;
    private final GenerateReceivableNotificationsUseCase generateReceivableNotificationsUseCase;
    private final AtomicReference<LocalDate> lastRunDate = new AtomicReference<>();

    public NotificationCollectionGenerationScheduler(
            SystemConfigRepository systemConfigRepository,
            GenerateReceivableNotificationsUseCase generateReceivableNotificationsUseCase
    ) {
        this.systemConfigRepository = systemConfigRepository;
        this.generateReceivableNotificationsUseCase = generateReceivableNotificationsUseCase;
    }

    @Scheduled(fixedDelayString = "${jurispay.scheduler.notifications.collection-generate.fixed-delay-ms:60000}")
    public void generateCollections() {
        var config = systemConfigRepository.findById(DEFAULT_SYSTEM_CONFIG_ID).orElse(null);
        if (config == null) {
            return;
        }

        ZoneId zoneId = ZoneId.of(config.getNotificationTimezone());
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        LocalDate today = now.toLocalDate();
        LocalTime dispatchTime = config.getCollectionDispatchTime();

        if (dispatchTime == null) {
            return;
        }

        if (now.toLocalTime().isBefore(dispatchTime)) {
            return;
        }

        LocalDate previous = lastRunDate.get();
        if (today.equals(previous)) {
            return;
        }

        generateReceivableNotificationsUseCase.generateCollections();
        lastRunDate.set(today);
    }
}
