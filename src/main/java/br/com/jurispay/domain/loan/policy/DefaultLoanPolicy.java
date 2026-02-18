package br.com.jurispay.domain.loan.policy;

import java.math.BigDecimal;

/**
 * Implementação padrão da política de empréstimo.
 * Define valores padrão de taxa de juros (30%) e multa diária (R$ 20,00).
 */
public class DefaultLoanPolicy implements LoanPolicy {

    private static final BigDecimal TAXA_JUROS_PADRAO = new BigDecimal("0.30");
    private static final BigDecimal MULTA_DIARIA_PADRAO = new BigDecimal("20.00");

    @Override
    public BigDecimal getInterestRate() {
        return TAXA_JUROS_PADRAO;
    }

    @Override
    public BigDecimal getDailyFine() {
        return MULTA_DIARIA_PADRAO;
    }
}

