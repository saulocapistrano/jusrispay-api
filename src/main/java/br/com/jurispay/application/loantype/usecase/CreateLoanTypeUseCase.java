package br.com.jurispay.application.loantype.usecase;

import br.com.jurispay.api.dto.loantype.LoanTypeRequest;
import br.com.jurispay.api.dto.loantype.LoanTypeResponse;

public interface CreateLoanTypeUseCase {
    LoanTypeResponse create(LoanTypeRequest request);
}
