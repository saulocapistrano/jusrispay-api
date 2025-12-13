package br.com.jurispay.infrastructure.persistence.jpa.repository;

import br.com.jurispay.infrastructure.persistence.jpa.entity.CollectionActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório Spring Data JPA para CollectionActionEntity.
 */
@Repository
public interface SpringDataCollectionRepository extends JpaRepository<CollectionActionEntity, Long> {

    List<CollectionActionEntity> findByLoanIdOrderByActionAtDesc(Long loanId);
}

