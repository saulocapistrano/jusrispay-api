package br.com.jurispay.application.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Resposta com item de empréstimo em atraso.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverdueLoanItemResponse {

    private Long loanId;

    private Long customerId;

    private Instant dueDate;

    private Long daysOverdue;

    private BigDecimal dailyFine;

    private BigDecimal totalFine;

    private BigDecimal expectedAmount;
}

