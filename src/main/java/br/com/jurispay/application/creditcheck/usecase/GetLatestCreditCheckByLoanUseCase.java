package br.com.jurispay.application.creditcheck.usecase;

import br.com.jurispay.application.creditcheck.dto.CreditCheckSummaryDto;

import java.util.Optional;

public interface GetLatestCreditCheckByLoanUseCase {

    Optional<CreditCheckSummaryDto> getByLoanId(Long loanId);
}
