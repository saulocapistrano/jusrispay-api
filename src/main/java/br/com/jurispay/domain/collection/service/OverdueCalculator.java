package br.com.jurispay.domain.collection.service;

import br.com.jurispay.domain.collection.model.OverdueInfo;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Serviço de domínio para cálculo de inadimplência.
 * Calcula dias em atraso e multa total baseado na data de vencimento.
 */
public class OverdueCalculator {

    /**
     * Calcula informações de inadimplência baseado na data de vencimento e data atual.
     *
     * @param dueDate data de vencimento do empréstimo
     * @param now data/hora atual para comparação
     * @param multaDiaria valor da multa diária
     * @return informações de inadimplência calculadas
     */
    public OverdueInfo calculate(Instant dueDate, Instant now, BigDecimal multaDiaria) {
        if (dueDate == null) {
            throw new IllegalArgumentException("Data de vencimento não pode ser nula.");
        }
        if (now == null) {
            throw new IllegalArgumentException("Data atual não pode ser nula.");
        }
        if (multaDiaria == null) {
            throw new IllegalArgumentException("Multa diária não pode ser nula.");
        }

        // Se now <= dueDate => daysOverdue=0 e fine=0
        if (now.isBefore(dueDate) || now.equals(dueDate)) {
            return OverdueInfo.builder()
                    .daysOverdue(0L)
                    .totalFine(BigDecimal.ZERO)
                    .build();
        }

        // Calcular número de dias completos em atraso
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, now);

        // Calcular multa total: multaDiaria * daysOverdue
        BigDecimal totalFine = multaDiaria.multiply(BigDecimal.valueOf(daysOverdue));

        return OverdueInfo.builder()
                .daysOverdue(daysOverdue)
                .totalFine(totalFine)
                .build();
    }
}

