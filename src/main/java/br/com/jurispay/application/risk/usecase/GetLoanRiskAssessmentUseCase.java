package br.com.jurispay.application.risk.usecase;

import br.com.jurispay.application.risk.dto.LoanRiskAssessmentResponse;

public interface GetLoanRiskAssessmentUseCase {

    LoanRiskAssessmentResponse getByLoanId(Long loanId);
}
