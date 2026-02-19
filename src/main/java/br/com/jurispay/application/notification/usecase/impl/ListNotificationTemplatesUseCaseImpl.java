package br.com.jurispay.application.notification.usecase.impl;

import br.com.jurispay.application.notification.dto.NotificationTemplateResponse;
import br.com.jurispay.application.notification.mapper.NotificationTemplateApplicationMapper;
import br.com.jurispay.application.notification.usecase.ListNotificationTemplatesUseCase;
import br.com.jurispay.infrastructure.notification.persistence.jpa.repository.SpringDataNotificationTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListNotificationTemplatesUseCaseImpl implements ListNotificationTemplatesUseCase {

    private final SpringDataNotificationTemplateRepository repository;
    private final NotificationTemplateApplicationMapper mapper;

    public ListNotificationTemplatesUseCaseImpl(
            SpringDataNotificationTemplateRepository repository,
            NotificationTemplateApplicationMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<NotificationTemplateResponse> listAll() {
        return repository.findAllByOrderByCodeAsc().stream().map(mapper::toResponse).toList();
    }
}
