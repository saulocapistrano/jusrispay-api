package br.com.jurispay.application.notification.usecase.impl;

import br.com.jurispay.application.notification.dto.NotificationTemplateResponse;
import br.com.jurispay.application.notification.dto.UpdateNotificationTemplateCommand;
import br.com.jurispay.application.notification.mapper.NotificationTemplateApplicationMapper;
import br.com.jurispay.application.notification.service.NotificationTemplateRenderer;
import br.com.jurispay.application.notification.usecase.UpdateNotificationTemplateUseCase;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.infrastructure.notification.persistence.jpa.repository.SpringDataNotificationTemplateRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateNotificationTemplateUseCaseImpl implements UpdateNotificationTemplateUseCase {

    private final SpringDataNotificationTemplateRepository repository;
    private final NotificationTemplateApplicationMapper mapper;
    private final NotificationTemplateRenderer templateRenderer;

    public UpdateNotificationTemplateUseCaseImpl(
            SpringDataNotificationTemplateRepository repository,
            NotificationTemplateApplicationMapper mapper,
            NotificationTemplateRenderer templateRenderer
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.templateRenderer = templateRenderer;
    }

    @Override
    public NotificationTemplateResponse update(UpdateNotificationTemplateCommand command) {
        if (command == null) {
            throw new ValidationException("Comando não pode ser nulo.");
        }
        if (command.getCode() == null || command.getCode().isBlank()) {
            throw new ValidationException("Código não pode ser vazio.");
        }
        if (command.getContent() == null || command.getContent().isBlank()) {
            throw new ValidationException("Conteúdo não pode ser vazio.");
        }
        if (command.getActive() == null) {
            throw new ValidationException("Ativo é obrigatório.");
        }

        try {
            templateRenderer.validateTemplate(command.getContent());
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(ex.getMessage());
        }

        var entity = repository.findByCode(command.getCode())
                .orElseThrow(() -> new NotFoundException("Template de notificação não encontrado"));

        entity.setContent(command.getContent());
        entity.setActive(command.getActive());

        var saved = repository.save(entity);
        return mapper.toResponse(saved);
    }
}
