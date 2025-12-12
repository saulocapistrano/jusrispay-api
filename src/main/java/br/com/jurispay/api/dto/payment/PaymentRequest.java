package br.com.jurispay.api.dto.payment;

import br.com.jurispay.domain.payment.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO de requisição para registro de pagamento.
 * Usado no endpoint POST /api/payments.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull(message = "ID do empréstimo é obrigatório")
    private Long loanId;

    @NotNull(message = "Valor pago é obrigatório")
    @Positive(message = "Valor pago deve ser maior que zero")
    private BigDecimal valorPago;

    private Instant dataPagamento; // Opcional: se null, será definido como Instant.now() no use case

    @NotNull(message = "Método de pagamento é obrigatório")
    private PaymentMethod metodo;
}

