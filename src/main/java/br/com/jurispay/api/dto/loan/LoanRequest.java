package br.com.jurispay.api.dto.loan;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import br.com.jurispay.domain.loan.model.LoanPaymentPeriod;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO de requisição para criação de empréstimo.
 * Usado no endpoint POST /api/loans.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRequest {

    @NotNull(message = "ID do cliente é obrigatório")
    private Long customerId;

    @NotNull(message = "ID do tipo de empréstimo é obrigatório")
    @Positive(message = "ID do tipo de empréstimo deve ser maior que zero")
    private Long loanTypeId;

    @NotNull(message = "Valor solicitado é obrigatório")
    @Positive(message = "Valor solicitado deve ser maior que zero")
    private BigDecimal valorSolicitado;

    @NotNull(message = "Taxa de juros é obrigatória")
    private BigDecimal taxaJuros;

    private LoanPaymentPeriod periodoPagamento;

    @NotNull(message = "Data prevista de devolução é obrigatória")
    private Instant dataPrevistaDevolucao;

    private Boolean allowKycIncomplete;
}

