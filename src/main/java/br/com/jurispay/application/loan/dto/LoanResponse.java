package br.com.jurispay.application.loan.dto;

import br.com.jurispay.domain.loan.model.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO de resposta de empréstimo para a camada API.
 * Não inclui dados pessoais do cliente, apenas o ID (LGPD).
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponse {

    private Long id;
    private Long customerId;
    private BigDecimal valorSolicitado;
    private BigDecimal valorDevolucaoPrevista;
    private BigDecimal taxaJuros;
    private BigDecimal multaDiaria;
    private LoanStatus status;
    private Instant dataLiberacao;
    private Instant dataPrevistaDevolucao;
    private Instant dataCriacao;
}

