package br.com.jurispay.application.report.usecase;

import br.com.jurispay.application.report.dto.OverdueLoanItemResponse;

import java.util.List;

/**
 * Caso de uso para listar empréstimos em atraso.
 */
public interface ListOverdueLoansUseCase {

    List<OverdueLoanItemResponse> list(int limit);
}

