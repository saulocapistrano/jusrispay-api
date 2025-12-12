package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.application.loan.dto.LoanCreationCommand;
import br.com.jurispay.application.loan.dto.LoanResponse;

/**
 * Caso de uso para criação de novo empréstimo.
 */
public interface CreateLoanUseCase {

    LoanResponse create(LoanCreationCommand command);
}

