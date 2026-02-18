package br.com.jurispay.api.dto.creditanalysis;

import br.com.jurispay.domain.creditanalysis.model.CreditAnalysisStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de requisição para decisão de análise de crédito.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreditAnalysisDecisionRequest {

    @NotNull(message = "ID do analista é obrigatório")
    private Long analystUserId;

    @NotNull(message = "Status da decisão é obrigatório")
    private CreditAnalysisStatus decisionStatus;

    private String rejectionReason;

    private String notes;

    private Boolean overrideCreditCheck;

    private String overrideReason;
}

