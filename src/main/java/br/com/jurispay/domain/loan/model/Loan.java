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

    /**
     * Marca o empréstimo como OVERDUE (em atraso).
     * Cria uma nova instância com status atualizado e dataAtualizacao atualizada.
     *
     * @return nova instância de Loan com status OVERDUE
     */
    public Loan markAsOverdue() {
        return Loan.builder()
                .id(this.id)
                .customerId(this.customerId)
                .valorSolicitado(this.valorSolicitado)
                .valorDevolucaoPrevista(this.valorDevolucaoPrevista)
                .taxaJuros(this.taxaJuros)
                .multaDiaria(this.multaDiaria)
                .dataLiberacao(this.dataLiberacao)
                .dataPrevistaDevolucao(this.dataPrevistaDevolucao)
                .dataCriacao(this.dataCriacao)
                .dataAtualizacao(Instant.now())
                .status(LoanStatus.OVERDUE)
                .build();
    }

    /**
     * Marca o empréstimo como PAID (pago).
     * Cria uma nova instância com status atualizado e dataAtualizacao atualizada.
     *
     * @return nova instância de Loan com status PAID
     */
    public Loan markAsPaid() {
        return Loan.builder()
                .id(this.id)
                .customerId(this.customerId)
                .valorSolicitado(this.valorSolicitado)
                .valorDevolucaoPrevista(this.valorDevolucaoPrevista)
                .taxaJuros(this.taxaJuros)
                .multaDiaria(this.multaDiaria)
                .dataLiberacao(this.dataLiberacao)
                .dataPrevistaDevolucao(this.dataPrevistaDevolucao)
                .dataCriacao(this.dataCriacao)
                .dataAtualizacao(Instant.now())
                .status(LoanStatus.PAID)
                .build();
    }
}

