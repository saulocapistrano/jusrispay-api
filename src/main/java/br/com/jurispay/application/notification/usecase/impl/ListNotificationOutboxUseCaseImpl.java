package br.com.jurispay.application.notification.usecase.impl;

import br.com.jurispay.application.notification.dto.NotificationOutboxResponse;
import br.com.jurispay.application.notification.mapper.NotificationOutboxApplicationMapper;
import br.com.jurispay.application.notification.usecase.ListNotificationOutboxUseCase;
import br.com.jurispay.infrastructure.notification.persistence.jpa.repository.SpringDataNotificationOutboxRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListNotificationOutboxUseCaseImpl implements ListNotificationOutboxUseCase {

    private final SpringDataNotificationOutboxRepository repository;
    private final NotificationOutboxApplicationMapper mapper;

    public ListNotificationOutboxUseCaseImpl(
            SpringDataNotificationOutboxRepository repository,
            NotificationOutboxApplicationMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<NotificationOutboxResponse> listTop(String status, Long customerId, Long loanId, Long receivableId) {
        var entities = findEntities(status, customerId, loanId, receivableId);
        return entities.stream().map(mapper::toResponse).toList();
    }

    private List<br.com.jurispay.infrastructure.notification.persistence.jpa.entity.NotificationOutboxEntity> findEntities(
            String status,
            Long customerId,
            Long loanId,
            Long receivableId
    ) {
        if (receivableId != null) {
            return repository.findTop200ByReceivableIdOrderByCreatedAtDesc(receivableId);
        }
        if (loanId != null) {
            return repository.findTop200ByLoanIdOrderByCreatedAtDesc(loanId);
        }
        if (customerId != null) {
            return repository.findTop200ByCustomerIdOrderByCreatedAtDesc(customerId);
        }
        if (status != null && !status.isBlank()) {
            return repository.findTop200ByStatusOrderByCreatedAtDesc(status.trim().toUpperCase());
        }
        return repository.findTop200ByOrderByCreatedAtDesc();
    }
}
