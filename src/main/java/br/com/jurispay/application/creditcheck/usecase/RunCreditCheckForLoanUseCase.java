package br.com.jurispay.application.creditcheck.usecase;

import br.com.jurispay.application.creditcheck.dto.CreditCheckSummaryDto;

public interface RunCreditCheckForLoanUseCase {

    CreditCheckSummaryDto run(Long loanId, Long requestedByUserId);
}
