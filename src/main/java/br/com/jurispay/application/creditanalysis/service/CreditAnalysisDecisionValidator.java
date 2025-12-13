package br.com.jurispay.application.creditanalysis.service;

import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysis;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysisStatus;
import org.springframework.stereotype.Component;

/**
 * Validador de decisão de análise de crédito na camada APPLICATION.
 * Encapsula as regras de validação para decisões de análise.
 */
@Component
public class CreditAnalysisDecisionValidator {

    /**
     * Valida as regras de negócio relacionadas à decisão da análise.
     *
     * @param analysis análise a ser validada
     * @throws ValidationException se alguma regra de negócio for violada
     */
    public void validateDecision(CreditAnalysis analysis) {
        if (analysis == null) {
            throw new ValidationException("Análise não pode ser nula.");
        }

        CreditAnalysisStatus status = analysis.getStatus();

        // Se status = REJECTED, rejectionReason é obrigatório
        if (status == CreditAnalysisStatus.REJECTED) {
            if (analysis.getRejectionReason() == null || analysis.getRejectionReason().trim().isEmpty()) {
                throw new ValidationException("Motivo da reprovação é obrigatório.");
            }
        }

        // Se status = APPROVED ou REJECTED, finishedAt é obrigatório
        if (status == CreditAnalysisStatus.APPROVED || status == CreditAnalysisStatus.REJECTED) {
            if (analysis.getFinishedAt() == null) {
                String message = status == CreditAnalysisStatus.APPROVED
                        ? "Data de finalização é obrigatória na aprovação."
                        : "Data de finalização é obrigatória na reprovação.";
                throw new ValidationException(message);
            }
        }
    }
}

