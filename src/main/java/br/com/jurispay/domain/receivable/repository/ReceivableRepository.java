package br.com.jurispay.domain.receivable.repository;

import br.com.jurispay.domain.receivable.model.Receivable;

import java.util.List;
import java.util.Optional;

public interface ReceivableRepository {

    List<Receivable> saveAll(List<Receivable> receivables);

    Receivable save(Receivable receivable);

    Optional<Receivable> findById(Long id);

    List<Receivable> findByLoanId(Long loanId);
}
