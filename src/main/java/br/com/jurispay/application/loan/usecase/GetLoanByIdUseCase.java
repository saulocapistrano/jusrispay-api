package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.application.loan.dto.LoanResponse;

/**
 * Caso de uso para buscar empréstimo por ID.
 */
public interface GetLoanByIdUseCase {

    LoanResponse getById(Long id);
}

