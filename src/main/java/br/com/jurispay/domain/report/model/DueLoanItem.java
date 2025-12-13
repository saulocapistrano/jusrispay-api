package br.com.jurispay.domain.report.model;

import br.com.jurispay.domain.loan.model.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Item de empréstimo com vencimento próximo.
 * Representa um empréstimo que está próximo da data de vencimento.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DueLoanItem {

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
     * Valor esperado de devolução.
     */
    private BigDecimal expectedAmount;

    /**
     * Status atual do empréstimo.
     */
    private LoanStatus status;
}

