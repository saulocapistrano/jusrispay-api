package br.com.jurispay.application.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Resposta com resumo do portfólio de empréstimos.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioSummaryResponse {

    private BigDecimal totalLoaned;

    private BigDecimal totalExpected;

    private BigDecimal totalReceived;

    private BigDecimal totalProfit;

    private BigDecimal roiPercent;

    private Long openLoans;

    private Long overdueLoans;

    private Long paidLoans;

    private Instant generatedAt;
}

