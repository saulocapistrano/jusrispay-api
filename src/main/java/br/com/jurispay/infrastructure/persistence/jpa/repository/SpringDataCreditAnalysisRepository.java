package br.com.jurispay.infrastructure.persistence.jpa.repository;

import br.com.jurispay.infrastructure.persistence.jpa.entity.CreditAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para CreditAnalysisEntity.
 */
@Repository
public interface SpringDataCreditAnalysisRepository extends JpaRepository<CreditAnalysisEntity, Long> {

    Optional<CreditAnalysisEntity> findByCustomerId(Long customerId);
}

