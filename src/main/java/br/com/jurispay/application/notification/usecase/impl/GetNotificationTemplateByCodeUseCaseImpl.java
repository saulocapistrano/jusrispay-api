package br.com.jurispay.application.notification.usecase.impl;

import br.com.jurispay.application.notification.dto.NotificationTemplateResponse;
import br.com.jurispay.application.notification.mapper.NotificationTemplateApplicationMapper;
import br.com.jurispay.application.notification.usecase.GetNotificationTemplateByCodeUseCase;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.infrastructure.notification.persistence.jpa.repository.SpringDataNotificationTemplateRepository;
import org.springframework.stereotype.Service;

@Service
public class GetNotificationTemplateByCodeUseCaseImpl implements GetNotificationTemplateByCodeUseCase {

    private final SpringDataNotificationTemplateRepository repository;
    private final NotificationTemplateApplicationMapper mapper;

    public GetNotificationTemplateByCodeUseCaseImpl(
            SpringDataNotificationTemplateRepository repository,
            NotificationTemplateApplicationMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public NotificationTemplateResponse getByCode(String code) {
        var entity = repository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Template de notificação não encontrado"));
        return mapper.toResponse(entity);
    }
}
