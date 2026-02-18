package br.com.jurispay.application.creditanalysis.usecase;

import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisDecisionCommand;
import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;

/**
 * Use case para decidir (aprovar/reprovar) uma análise de crédito.
 */
public interface DecideCreditAnalysisUseCase {

    /**
     * Decide sobre uma análise de crédito (aprova ou reprova).
     *
     * @param command comando com dados da decisão
     * @return resposta com informações da análise atualizada
     */
    CreditAnalysisResponse decide(CreditAnalysisDecisionCommand command);
}

