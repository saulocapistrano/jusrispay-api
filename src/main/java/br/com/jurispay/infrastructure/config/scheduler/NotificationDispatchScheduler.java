package br.com.jurispay.infrastructure.config.scheduler;

import br.com.jurispay.application.notification.usecase.DispatchNotificationOutboxUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationDispatchScheduler {

    private final DispatchNotificationOutboxUseCase dispatchNotificationOutboxUseCase;

    public NotificationDispatchScheduler(DispatchNotificationOutboxUseCase dispatchNotificationOutboxUseCase) {
        this.dispatchNotificationOutboxUseCase = dispatchNotificationOutboxUseCase;
    }

    @Scheduled(fixedDelayString = "${jurispay.scheduler.notifications.dispatch.fixed-delay-ms:30000}")
    public void dispatch() {
        dispatchNotificationOutboxUseCase.dispatch();
    }
}
