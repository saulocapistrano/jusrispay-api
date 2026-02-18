package br.com.jurispay.application.report.usecase;

import br.com.jurispay.application.report.dto.PortfolioSummaryResponse;

/**
 * Caso de uso para obter resumo do portfólio de empréstimos.
 */
public interface GetPortfolioSummaryUseCase {

    PortfolioSummaryResponse get();
}

