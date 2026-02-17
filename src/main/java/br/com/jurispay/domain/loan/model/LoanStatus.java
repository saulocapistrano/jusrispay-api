package br.com.jurispay.domain.loan.model;

/**
 * Enum que representa os possíveis status de um empréstimo.
 */
public enum LoanStatus {
    /**
     * Solicitação de empréstimo criada e aguardando análise.
     */
    REQUESTED,

    /**
     * Solicitação criada, porém pendente de documentos/validação de KYC.
     */
    PENDING_DOCUMENTS,

    APPROVED,

    CREDITED,

    /**
     * Empréstimo criado/ativo.
     */
    OPEN,

    /**
     * Empréstimo quitado.
     */
    PAID,

    /**
     * Empréstimo em atraso.
     */
    OVERDUE,

    /**
     * Empréstimo cancelado.
     */
    CANCELED,

    /**
     * Solicitação reprovada na análise.
     */
    REJECTED
}

