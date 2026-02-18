package br.com.jurispay.application.creditanalysis.usecase;

import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;

/**
 * Use case para buscar análise de crédito por cliente.
 */
public interface GetCreditAnalysisByCustomerUseCase {

    /**
     * Busca análise de crédito por ID do cliente.
     *
     * @param customerId ID do cliente
     * @return resposta com informações da análise
     */
    CreditAnalysisResponse getByCustomerId(Long customerId);
}

