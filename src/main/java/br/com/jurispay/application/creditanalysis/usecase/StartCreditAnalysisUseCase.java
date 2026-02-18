package br.com.jurispay.application.creditanalysis.usecase;

import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;
import br.com.jurispay.application.creditanalysis.dto.StartCreditAnalysisCommand;

/**
 * Use case para iniciar uma análise de crédito.
 */
public interface StartCreditAnalysisUseCase {

    /**
     * Inicia uma análise de crédito para um cliente.
     *
     * @param command comando com dados para iniciar a análise
     * @return resposta com informações da análise iniciada
     */
    CreditAnalysisResponse start(StartCreditAnalysisCommand command);
}

