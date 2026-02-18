package br.com.jurispay.domain.creditdecisionoverride.repository;

import br.com.jurispay.domain.creditdecisionoverride.model.CreditDecisionOverride;

import java.util.Optional;

public interface CreditDecisionOverrideRepository {

    CreditDecisionOverride save(CreditDecisionOverride override);

    Optional<CreditDecisionOverride> findLatestByLoanId(Long loanId);
}
