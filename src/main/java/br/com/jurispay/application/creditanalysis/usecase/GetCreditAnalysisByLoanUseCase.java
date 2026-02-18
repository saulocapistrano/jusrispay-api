package br.com.jurispay.application.creditanalysis.usecase;

import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;

/**
 * Use case para buscar análise de crédito por empréstimo.
 */
public interface GetCreditAnalysisByLoanUseCase {

    /**
     * Busca análise de crédito por ID do empréstimo.
     *
     * @param loanId ID do empréstimo
     * @return resposta com informações da análise
     */
    CreditAnalysisResponse getByLoanId(Long loanId);
}
