package br.com.jurispay.domain.loantype.repository;

import br.com.jurispay.domain.loantype.model.LoanType;

import java.util.List;
import java.util.Optional;

public interface LoanTypeRepository {

    LoanType save(LoanType loanType);

    Optional<LoanType> findById(Long id);

    List<LoanType> findAll();

    List<LoanType> findAllActive();

    boolean existsByNomeIgnoreCase(String nome);
}
