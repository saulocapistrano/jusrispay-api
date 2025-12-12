package br.com.jurispay.infrastructure.persistence.jpa;

import br.com.jurispay.domain.document.model.Document;
import br.com.jurispay.domain.document.repository.DocumentRepository;
import br.com.jurispay.infrastructure.persistence.jpa.entity.DocumentEntity;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataDocumentRepository;
import br.com.jurispay.infrastructure.persistence.mapper.DocumentEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter JPA para DocumentRepository.
 * Implementa a porta de repositório do domínio usando Spring Data JPA.
 */
@Component
public class DocumentRepositoryAdapter implements DocumentRepository {

    private final SpringDataDocumentRepository springDataRepository;
    private final DocumentEntityMapper mapper;

    public DocumentRepositoryAdapter(
            SpringDataDocumentRepository springDataRepository,
            DocumentEntityMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Document save(Document document) {
        DocumentEntity entity = mapper.toEntity(document);
        DocumentEntity savedEntity = springDataRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Document> findById(Long id) {
        return springDataRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Document> findAll() {
        return springDataRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Document> findByCustomerId(Long customerId) {
        return springDataRepository.findByCustomerId(customerId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Document> findByLoanId(Long loanId) {
        return springDataRepository.findByLoanId(loanId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}

