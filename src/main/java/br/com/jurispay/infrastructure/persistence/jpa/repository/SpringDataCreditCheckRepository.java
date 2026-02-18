package br.com.jurispay.infrastructure.persistence.jpa.repository;

import br.com.jurispay.domain.creditcheck.model.CreditCheckStatus;
import br.com.jurispay.infrastructure.persistence.jpa.entity.CreditCheckEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface SpringDataCreditCheckRepository extends JpaRepository<CreditCheckEntity, Long> {

    Optional<CreditCheckEntity> findFirstByCpfAndStatusAndFinishedAtAfterOrderByFinishedAtDesc(
            String cpf,
            CreditCheckStatus status,
            Instant finishedAt);

    Optional<CreditCheckEntity> findFirstByLoanIdOrderByCreatedAtDesc(Long loanId);
}
