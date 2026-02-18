package br.com.jurispay.domain.loan.factory;

import lombok.Builder;
import lombok.Getter;

import br.com.jurispay.domain.loan.model.LoanPaymentPeriod;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Value Object que representa os dados necessários para criação de um Loan.
 * Encapsula os dados de entrada da criação de empréstimo.
 */
@Getter
@Builder
public class LoanCreationData {

    /**
     * ID do cliente que solicita o empréstimo.
     */
    private final Long customerId;

    /**
     * ID do tipo de empréstimo (loanType).
     */
    private final Long loanTypeId;

    /**
     * Valor solicitado pelo cliente.
     */
    private final BigDecimal valorSolicitado;

    /**
     * Taxa de juros do empréstimo.
     * Ex: 0.30 para 30%
     */
    private final BigDecimal taxaJuros;

    /**
     * Período de pagamento do empréstimo.
     */
    private final LoanPaymentPeriod periodoPagamento;

    /**
     * Data prevista para devolução do empréstimo.
     */
    private final Instant dataPrevistaDevolucao;
}

