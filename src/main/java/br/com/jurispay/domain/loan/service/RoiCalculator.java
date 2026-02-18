package br.com.jurispay.domain.loan.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Serviço de domínio para cálculo de ROI (Return on Investment).
 * Centraliza a lógica de cálculo de ROI para evitar duplicação.
 */
public class RoiCalculator {

    /**
     * Calcula o ROI percentual baseado no lucro e no valor investido.
     *
     * @param profit lucro (valor recebido - valor investido)
     * @param investedAmount valor investido (valor solicitado)
     * @return ROI percentual (0 se investedAmount for zero ou negativo)
     */
    public BigDecimal calculateRoiPercent(BigDecimal profit, BigDecimal investedAmount) {
        if (investedAmount == null || investedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        if (profit == null) {
            return BigDecimal.ZERO;
        }

        return profit
                .divide(investedAmount, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcula o ROI em BRL (valor recebido - valor investido).
     *
     * @param receivedAmount valor recebido
     * @param investedAmount valor investido
     * @return ROI em BRL
     */
    public BigDecimal calculateRoiBrl(BigDecimal receivedAmount, BigDecimal investedAmount) {
        if (receivedAmount == null || investedAmount == null) {
            return BigDecimal.ZERO;
        }
        return receivedAmount.subtract(investedAmount);
    }
}

