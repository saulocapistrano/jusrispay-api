package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.application.loan.dto.LoanResponse;

import java.util.List;

/**
 * Caso de uso para listar todos os empréstimos.
 */
public interface ListLoansUseCase {

    List<LoanResponse> listAll();
}

