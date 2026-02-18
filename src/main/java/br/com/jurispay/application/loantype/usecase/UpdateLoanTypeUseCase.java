package br.com.jurispay.application.loantype.usecase;

import br.com.jurispay.api.dto.loantype.LoanTypeRequest;
import br.com.jurispay.api.dto.loantype.LoanTypeResponse;

public interface UpdateLoanTypeUseCase {
    LoanTypeResponse update(Long id, LoanTypeRequest request);
}
