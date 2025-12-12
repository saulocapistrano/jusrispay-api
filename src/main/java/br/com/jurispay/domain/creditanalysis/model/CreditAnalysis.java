package br.com.jurispay.domain.creditanalysis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Modelo de domínio para Análise de Crédito.
 * Controla o workflow de aprovação e registro de auditoria.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditAnalysis {

    private Long id;

    /**
     * ID do cliente sendo analisado.
     */
    private Long customerId;

    /**
     * Status atual da análise.
     */
    private CreditAnalysisStatus status;

    /**
     * ID do usuário do sistema que realizou a análise.
     * Não contém dados sensíveis, apenas referência.
     */
    private Long analystUserId;

    /**
     * Data/hora de início da análise.
     */
    private Instant startedAt;

    /**
     * Data/hora de finalização da análise (aprovação ou reprovação).
     */
    private Instant finishedAt;

    /**
     * Prazo para decisão (SLA de 24h a partir do início).
     */
    private Instant decisionDeadlineAt;

    /**
     * Motivo da reprovação (obrigatório se status = REJECTED).
     */
    private String rejectionReason;

    /**
     * Observações internas sobre a análise.
     */
    private String notes;

    /**
     * Data/hora de criação do registro.
     */
    private Instant createdAt;

    /**
     * Data/hora da última atualização.
     */
    private Instant updatedAt;
}

