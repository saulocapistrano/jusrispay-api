package br.com.jurispay.application.notification.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class NotificationTemplateResponse {

    private Long id;
    private String code;
    private String channel;
    private String content;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
