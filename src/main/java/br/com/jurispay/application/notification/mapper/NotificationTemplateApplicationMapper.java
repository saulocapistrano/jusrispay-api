package br.com.jurispay.application.notification.mapper;

import br.com.jurispay.application.notification.dto.NotificationTemplateResponse;
import br.com.jurispay.infrastructure.notification.persistence.jpa.entity.NotificationTemplateEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationTemplateApplicationMapper {

    public NotificationTemplateResponse toResponse(NotificationTemplateEntity entity) {
        if (entity == null) {
            return null;
        }
        return NotificationTemplateResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .channel(entity.getChannel())
                .content(entity.getContent())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
