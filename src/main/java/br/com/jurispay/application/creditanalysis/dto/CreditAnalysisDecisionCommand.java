package br.com.jurispay.application.creditanalysis.dto;

import br.com.jurispay.domain.creditanalysis.model.CreditAnalysisStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Comando para decidir (aprovar/reprovar) uma análise de crédito.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditAnalysisDecisionCommand {

    private Long loanId;

    private Long analystUserId;

    /**
     * Status da decisão (APPROVED ou REJECTED).
     */
    private CreditAnalysisStatus decisionStatus;

    /**
     * Motivo da reprovação (obrigatório se decisionStatus = REJECTED).
     */
    private String rejectionReason;

    /**
     * Observações sobre a decisão.
     */
    private String notes;
}

