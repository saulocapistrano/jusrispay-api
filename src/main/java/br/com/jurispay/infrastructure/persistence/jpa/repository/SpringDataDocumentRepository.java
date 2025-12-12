package br.com.jurispay.infrastructure.persistence.jpa.repository;

import br.com.jurispay.infrastructure.persistence.jpa.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório Spring Data JPA para DocumentEntity.
 */
public interface SpringDataDocumentRepository extends JpaRepository<DocumentEntity, Long> {

    List<DocumentEntity> findByCustomerId(Long customerId);

    List<DocumentEntity> findByLoanId(Long loanId);
}

