package br.com.jurispay.application.notification.usecase.impl;

import br.com.jurispay.application.notification.service.NotificationTemplateRenderer;
import br.com.jurispay.application.notification.usecase.PreviewNotificationTemplateUseCase;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.infrastructure.notification.persistence.jpa.repository.SpringDataNotificationTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PreviewNotificationTemplateUseCaseImpl implements PreviewNotificationTemplateUseCase {

    private final SpringDataNotificationTemplateRepository repository;
    private final NotificationTemplateRenderer renderer;

    public PreviewNotificationTemplateUseCaseImpl(
            SpringDataNotificationTemplateRepository repository,
            NotificationTemplateRenderer renderer
    ) {
        this.repository = repository;
        this.renderer = renderer;
    }

    @Override
    public String preview(String code, Map<String, String> values) {
        if (code == null || code.isBlank()) {
            throw new ValidationException("Código não pode ser vazio.");
        }

        var template = repository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Template de notificação não encontrado"));

        try {
            return renderer.render(template.getContent(), values == null ? Map.of() : values);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(ex.getMessage());
        }
    }
}
