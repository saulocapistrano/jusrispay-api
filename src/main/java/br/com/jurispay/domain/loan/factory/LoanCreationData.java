package br.com.jurispay.domain.loan.factory;

import lombok.Builder;
import lombok.Getter;

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
     * Valor solicitado pelo cliente.
     */
    private final BigDecimal valorSolicitado;

    /**
     * Data prevista para devolução do empréstimo.
     */
    private final Instant dataPrevistaDevolucao;
}

