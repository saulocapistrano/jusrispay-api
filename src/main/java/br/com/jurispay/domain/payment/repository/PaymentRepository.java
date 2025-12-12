package br.com.jurispay.domain.payment.repository;

import br.com.jurispay.domain.payment.model.Payment;

import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório do domínio Payment.
 * Define as operações de persistência sem depender de implementação específica.
 */
public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(Long id);

    List<Payment> findAll();

    List<Payment> findByLoanId(Long loanId);
}

