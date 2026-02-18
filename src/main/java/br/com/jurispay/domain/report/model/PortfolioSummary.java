package br.com.jurispay.domain.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Resumo do portfólio de empréstimos.
 * Agrega estatísticas financeiras e contadores de status.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioSummary {

    /**
     * Soma total dos valores solicitados em todos os empréstimos.
     */
    private BigDecimal totalLoaned;

    /**
     * Soma total dos valores de devolução previstos.
     */
    private BigDecimal totalExpected;

    /**
     * Soma total dos valores recebidos em pagamentos.
     */
    private BigDecimal totalReceived;

    /**
     * Lucro total (totalReceived - totalLoaned).
     */
    private BigDecimal totalProfit;

    /**
     * ROI percentual: (totalProfit / totalLoaned) * 100.
     * Zero se totalLoaned = 0.
     */
    private BigDecimal roiPercent;

    /**
     * Quantidade de empréstimos com status OPEN.
     */
    private Long openLoans;

    /**
     * Quantidade de empréstimos com status OVERDUE.
     */
    private Long overdueLoans;

    /**
     * Quantidade de empréstimos com status PAID.
     */
    private Long paidLoans;

    /**
     * Data/hora de geração do relatório.
     */
    private Instant generatedAt;
}

