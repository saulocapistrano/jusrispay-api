package br.com.jurispay.domain.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entidade de domínio Payment.
 * Representa um pagamento/recebimento no sistema Jurispay.
 * Não contém anotações JPA - é domínio puro.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    private Long id;
    private Long loanId;
    private BigDecimal valorPago;
    private Instant dataPagamento;
    private int diasAtraso;
    private BigDecimal multaTotal;
    private Long fineId;
    private Integer fineTimes;
    private BigDecimal fineUnitAmount;
    private BigDecimal fineTotalAmount;
    private BigDecimal valorFinalRecebido;
    private BigDecimal roiBrl;
    private BigDecimal roiPercent;
    private PaymentMethod metodo;
    private Instant dataCriacao;
}

