package br.com.jurispay.domain.loan.repository;

import br.com.jurispay.domain.loan.model.Loan;

import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório do domínio Loan.
 * Define as operações de persistência sem depender de implementação específica.
 */
public interface LoanRepository {

    Loan save(Loan loan);

    Optional<Loan> findById(Long id);

    List<Loan> findAll();
}

