package br.com.jurispay.infrastructure.persistence.jpa;

import br.com.jurispay.domain.collection.model.CollectionAction;
import br.com.jurispay.domain.collection.repository.CollectionRepository;
import br.com.jurispay.infrastructure.persistence.jpa.entity.CollectionActionEntity;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataCollectionRepository;
import br.com.jurispay.infrastructure.persistence.mapper.CollectionEntityMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa CollectionRepository do domínio usando JPA.
 * Converte entre entidades JPA e modelos de domínio.
 */
@Component
public class CollectionRepositoryAdapter implements CollectionRepository {

    private final SpringDataCollectionRepository springDataCollectionRepository;
    private final CollectionEntityMapper mapper;

    public CollectionRepositoryAdapter(
            SpringDataCollectionRepository springDataCollectionRepository,
            CollectionEntityMapper mapper) {
        this.springDataCollectionRepository = springDataCollectionRepository;
        this.mapper = mapper;
    }

    @Override
    public CollectionAction save(CollectionAction action) {
        CollectionActionEntity entity = mapper.toEntity(action);
        CollectionActionEntity savedEntity = springDataCollectionRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<CollectionAction> findById(Long id) {
        return springDataCollectionRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<CollectionAction> findByLoanId(Long loanId) {
        return springDataCollectionRepository.findByLoanIdOrderByActionAtDesc(loanId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Instant> findLastActionAtByLoanIds(List<Long> loanIds) {
        if (loanIds == null || loanIds.isEmpty()) {
            return Optional.empty();
        }
        return springDataCollectionRepository.findMaxActionAtByLoanIds(loanIds);
    }
}
