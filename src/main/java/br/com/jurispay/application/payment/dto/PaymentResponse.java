package br.com.jurispay.application.payment.dto;

import br.com.jurispay.domain.payment.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO de resposta de pagamento para a camada API.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long loanId;
    private BigDecimal valorPago;
    private Instant dataPagamento;
    private int diasAtraso;
    private BigDecimal multaTotal;
    private BigDecimal valorFinalRecebido;
    private BigDecimal roiBrl;
    private BigDecimal roiPercent;
    private PaymentMethod metodo;
    private Instant dataCriacao;
}

