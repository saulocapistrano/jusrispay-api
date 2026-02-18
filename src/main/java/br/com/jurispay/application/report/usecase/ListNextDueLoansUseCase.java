package br.com.jurispay.application.report.usecase;

import br.com.jurispay.application.report.dto.DueLoanItemResponse;

import java.util.List;

/**
 * Caso de uso para listar empréstimos com vencimento próximo.
 */
public interface ListNextDueLoansUseCase {

    List<DueLoanItemResponse> list(int limit);
}

