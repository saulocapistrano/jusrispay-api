package br.com.jurispay.domain.payment.model;

/**
 * Enum que representa os métodos de pagamento disponíveis.
 */
public enum PaymentMethod {
    /**
     * Pagamento via PIX.
     */
    PIX,

    /**
     * Pagamento em dinheiro.
     */
    CASH,

    /**
     * Pagamento via transferência bancária.
     */
    TRANSFER,

    /**
     * Outro método de pagamento.
     */
    OTHER
}

