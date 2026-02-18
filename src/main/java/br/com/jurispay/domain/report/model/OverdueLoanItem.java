package br.com.jurispay.domain.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Item de empréstimo em atraso.
 * Representa um empréstimo que está vencido com informações de multa calculada.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverdueLoanItem {

    /**
     * ID do empréstimo.
     */
    private Long loanId;

    /**
     * ID do cliente.
     */
    private Long customerId;

    /**
     * Data de vencimento do empréstimo.
     */
    private Instant dueDate;

    /**
     * Número de dias em atraso.
     */
    private Long daysOverdue;

    /**
     * Valor da multa diária.
     */
    private BigDecimal dailyFine;

    /**
     * Valor total da multa acumulada.
     */
    private BigDecimal totalFine;

    /**
     * Valor esperado de devolução (incluindo multa).
     */
    private BigDecimal expectedAmount;
}

