package br.com.jurispay.domain.document.model;

/**
 * Status de validação do documento.
 */
public enum DocumentStatus {
    UPLOADED,   // arquivo enviado, aguardando validação
    VALIDATED,  // aprovado/validado pelo analista
    REJECTED    // reprovado
}

