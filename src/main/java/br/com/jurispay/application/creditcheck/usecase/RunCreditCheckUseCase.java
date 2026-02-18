package br.com.jurispay.application.creditcheck.usecase;

import br.com.jurispay.application.creditcheck.dto.CreditCheckSummaryDto;

public interface RunCreditCheckUseCase {

    CreditCheckSummaryDto run(Long loanId, Long customerId, String cpf, Long requestedByUserId);
}
