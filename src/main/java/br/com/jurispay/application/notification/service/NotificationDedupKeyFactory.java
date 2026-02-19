package br.com.jurispay.application.notification.service;

import br.com.jurispay.domain.notification.model.NotificationType;

import java.time.LocalDate;

public final class NotificationDedupKeyFactory {

    private NotificationDedupKeyFactory() {
    }

    public static String forReceivable(NotificationType type, Long receivableId, LocalDate referenceDate) {
        return "receivable:" + receivableId + ":type:" + type.name() + ":date:" + referenceDate;
    }
}
