package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.application.loan.dto.LoanResponse;

/**
 * Sincroniza o status do empréstimo (Loan) com a decisão final da análise de crédito.
 *
 * Útil para corrigir inconsistências históricas onde a análise foi finalizada,
 * mas o Loan permaneceu com status REQUESTED.
 */
public interface SyncLoanStatusFromCreditAnalysisUseCase {

    LoanResponse sync(Long loanId);
}
