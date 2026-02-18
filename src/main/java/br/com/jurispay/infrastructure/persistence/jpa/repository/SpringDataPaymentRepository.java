package br.com.jurispay.infrastructure.persistence.jpa.repository;

import br.com.jurispay.infrastructure.persistence.jpa.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data JPA para PaymentEntity.
 */
@Repository
public interface SpringDataPaymentRepository extends JpaRepository<PaymentEntity, Long> {

    List<PaymentEntity> findByLoan_Id(Long loanId);

    @Query("select max(p.dataPagamento) from PaymentEntity p where p.loan.id in :loanIds")
    Optional<Instant> findMaxPaymentAtByLoanIds(@Param("loanIds") List<Long> loanIds);
}
