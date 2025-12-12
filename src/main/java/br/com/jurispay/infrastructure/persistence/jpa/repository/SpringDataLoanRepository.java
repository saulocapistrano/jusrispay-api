package br.com.jurispay.infrastructure.persistence.jpa.repository;

import br.com.jurispay.infrastructure.persistence.jpa.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data JPA para LoanEntity.
 */
@Repository
public interface SpringDataLoanRepository extends JpaRepository<LoanEntity, Long> {
}

