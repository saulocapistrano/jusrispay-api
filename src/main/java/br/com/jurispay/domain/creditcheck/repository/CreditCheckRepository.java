package br.com.jurispay.domain.creditcheck.repository;

import br.com.jurispay.domain.creditcheck.model.CreditCheck;

import java.time.Instant;
import java.util.Optional;

public interface CreditCheckRepository {

    CreditCheck save(CreditCheck creditCheck);

    Optional<CreditCheck> findLatestCompletedByCpfSince(String cpf, Instant since);

    Optional<CreditCheck> findLatestByLoanId(Long loanId);
}
