package br.com.jurispay.domain.notification.model;

public enum NotificationOutboxStatus {
    PENDING,
    SENT,
    FAILED,
    RETRY,
    CANCELED
}
