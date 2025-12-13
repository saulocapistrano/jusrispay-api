package br.com.jurispay.application.collection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Resposta com informações de inadimplência calculadas.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverdueInfoResponse {

    private Long loanId;

    private Long daysOverdue;

    private BigDecimal dailyFine;

    private BigDecimal totalFine;

    private Instant dueDate;

    private Instant calculatedAt;
}

