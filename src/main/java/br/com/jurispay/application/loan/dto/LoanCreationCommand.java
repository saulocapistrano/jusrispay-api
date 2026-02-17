package br.com.jurispay.application.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import br.com.jurispay.domain.loan.model.LoanPaymentPeriod;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO de comando para criação de novo empréstimo.
 * Representa os dados necessários para criar um empréstimo.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanCreationCommand {

    private Long customerId;
    private BigDecimal valorSolicitado;
    private BigDecimal taxaJuros;
    private LoanPaymentPeriod periodoPagamento;
    private Instant dataPrevistaDevolucao;

    private Boolean allowKycIncomplete;
}

