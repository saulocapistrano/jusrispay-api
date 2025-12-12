package br.com.jurispay.domain.loan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entidade de domínio Loan.
 * Representa um empréstimo no sistema Jurispay.
 * Não contém anotações JPA - é domínio puro.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    private Long id;
    private Long customerId;
    private BigDecimal valorSolicitado;
    private BigDecimal valorDevolucaoPrevista;
    private BigDecimal taxaJuros;
    private BigDecimal multaDiaria;
    private Instant dataLiberacao;
    private Instant dataPrevistaDevolucao;
    private Instant dataCriacao;
    private Instant dataAtualizacao;
    private LoanStatus status;
}

