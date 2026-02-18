package br.com.jurispay.domain.creditanalysis.model;

/**
 * Status do processo de análise de crédito.
 */
public enum CreditAnalysisStatus {
    PENDING,     // criado, ainda não iniciou análise
    IN_REVIEW,   // análise iniciada
    APPROVED,    // aprovado
    REJECTED     // reprovado
}

