package br.com.jurispay.domain.loan.model;

/**
 * Enum que representa os possíveis status de um empréstimo.
 */
public enum LoanStatus {
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
    CANCELED
}

