package br.com.jurispay.application.creditanalysis.dto;

import br.com.jurispay.domain.creditanalysis.model.CreditAnalysisStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Resposta padrão de análise de crédito.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditAnalysisResponse {

    private Long id;

    private Long loanId;

    private Long customerId;

    private CreditAnalysisStatus status;

    private Long analystUserId;

    private Instant startedAt;

    private Instant finishedAt;

    private Instant decisionDeadlineAt;

    private String rejectionReason;

    private String notes;
}

