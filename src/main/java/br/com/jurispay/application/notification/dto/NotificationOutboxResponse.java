package br.com.jurispay.application.notification.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class NotificationOutboxResponse {

    private Long id;
    private String dedupKey;
    private String type;
    private String channel;
    private String status;

    private Long customerId;
    private Long loanId;
    private Long receivableId;

    private String toPhone;
    private String templateCode;
    private String renderedMessage;

    private String providerMessageId;

    private int attempts;
    private Instant nextAttemptAt;
    private String lastError;
    private Instant sentAt;

    private Instant createdAt;
    private Instant updatedAt;
}
