package br.com.jurispay.application.payment.dto;

import br.com.jurispay.domain.payment.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO de comando para registro de novo pagamento.
 * Representa os dados necessários para registrar um pagamento.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRegistrationCommand {

    private Long loanId;
    private BigDecimal valorPago;
    private Instant dataPagamento; // Opcional: se null, será definido como Instant.now() no use case
    private PaymentMethod metodo;
}

