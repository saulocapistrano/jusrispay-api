package br.com.jurispay.application.notification.mapper;

import br.com.jurispay.application.notification.dto.NotificationOutboxResponse;
import br.com.jurispay.infrastructure.notification.persistence.jpa.entity.NotificationOutboxEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationOutboxApplicationMapper {

    public NotificationOutboxResponse toResponse(NotificationOutboxEntity entity) {
        if (entity == null) {
            return null;
        }

        return NotificationOutboxResponse.builder()
                .id(entity.getId())
                .dedupKey(entity.getDedupKey())
                .type(entity.getType())
                .channel(entity.getChannel())
                .status(entity.getStatus())
                .customerId(entity.getCustomerId())
                .loanId(entity.getLoanId())
                .receivableId(entity.getReceivableId())
                .toPhone(entity.getToPhone())
                .templateCode(entity.getTemplateCode())
                .renderedMessage(entity.getRenderedMessage())
                .providerMessageId(entity.getProviderMessageId())
                .attempts(entity.getAttempts())
                .nextAttemptAt(entity.getNextAttemptAt())
                .lastError(entity.getLastError())
                .sentAt(entity.getSentAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
