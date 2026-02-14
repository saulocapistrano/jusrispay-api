package br.com.jurispay.domain.receivable.repository;

import br.com.jurispay.domain.receivable.model.Receivable;

import java.util.List;

public interface ReceivableRepository {

    List<Receivable> saveAll(List<Receivable> receivables);

    List<Receivable> findByLoanId(Long loanId);
}
