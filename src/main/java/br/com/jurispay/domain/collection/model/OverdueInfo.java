package br.com.jurispay.domain.collection.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Informações sobre inadimplência calculadas.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverdueInfo {

    /**
     * Número de dias completos em atraso.
     */
    private long daysOverdue;

    /**
     * Valor total da multa calculada.
     */
    private BigDecimal totalFine;
}

