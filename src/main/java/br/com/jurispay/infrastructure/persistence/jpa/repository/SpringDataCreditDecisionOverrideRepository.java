package br.com.jurispay.infrastructure.persistence.jpa.repository;

import br.com.jurispay.infrastructure.persistence.jpa.entity.CreditDecisionOverrideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataCreditDecisionOverrideRepository extends JpaRepository<CreditDecisionOverrideEntity, Long> {

    Optional<CreditDecisionOverrideEntity> findFirstByLoanIdOrderByCreatedAtDesc(Long loanId);
}
