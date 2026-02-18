package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.application.loan.dto.LoanResponse;

public interface CreditLoanUseCase {

    LoanResponse credit(Long loanId);
}
