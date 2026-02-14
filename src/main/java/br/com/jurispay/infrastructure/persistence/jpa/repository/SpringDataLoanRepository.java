package br.com.jurispay.infrastructure.persistence.jpa.repository;

import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.infrastructure.persistence.jpa.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório Spring Data JPA para LoanEntity.
 */
@Repository
public interface SpringDataLoanRepository extends JpaRepository<LoanEntity, Long> {

    List<LoanEntity> findByStatus(LoanStatus status);

    List<LoanEntity> findByCustomer_Id(Long customerId);
}
