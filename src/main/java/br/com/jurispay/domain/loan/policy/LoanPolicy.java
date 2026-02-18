package br.com.jurispay.domain.loan.policy;

import java.math.BigDecimal;

/**
 * Interface Strategy para políticas de empréstimo.
 * Define taxas de juros e multas diárias que podem variar por perfil ou regra de negócio.
 */
public interface LoanPolicy {

    /**
     * Retorna a taxa de juros a ser aplicada no empréstimo.
     *
     * @return taxa de juros (ex: 0.30 para 30%)
     */
    BigDecimal getInterestRate();

    /**
     * Retorna o valor da multa diária por atraso.
     *
     * @return multa diária em BRL
     */
    BigDecimal getDailyFine();
}

