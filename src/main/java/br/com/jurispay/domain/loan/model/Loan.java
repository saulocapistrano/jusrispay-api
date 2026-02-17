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
    private Long loanTypeId;
    private BigDecimal valorSolicitado;
    private BigDecimal valorDevolucaoPrevista;
    private BigDecimal taxaJuros;
    private BigDecimal multaDiaria;
    private LoanPaymentPeriod periodoPagamento;
    private Integer quantidadeParcelas;
    private BigDecimal valorParcela;
    private Instant dataLiberacao;
    private Instant dataPrevistaDevolucao;
    private Instant dataCriacao;
    private Instant dataAtualizacao;
    private LoanStatus status;

    public Loan approve(Instant now) {
        return Loan.builder()
                .id(this.id)
                .customerId(this.customerId)
                .loanTypeId(this.loanTypeId)
                .valorSolicitado(this.valorSolicitado)
                .valorDevolucaoPrevista(this.valorDevolucaoPrevista)
                .taxaJuros(this.taxaJuros)
                .multaDiaria(this.multaDiaria)
                .periodoPagamento(this.periodoPagamento)
                .quantidadeParcelas(this.quantidadeParcelas)
                .valorParcela(this.valorParcela)
                .dataLiberacao(null)
                .dataPrevistaDevolucao(this.dataPrevistaDevolucao)
                .dataCriacao(this.dataCriacao)
                .dataAtualizacao(now)
                .status(LoanStatus.APPROVED)
                .build();
    }

    public Loan credit(Instant now) {
        return Loan.builder()
                .id(this.id)
                .customerId(this.customerId)
                .loanTypeId(this.loanTypeId)
                .valorSolicitado(this.valorSolicitado)
                .valorDevolucaoPrevista(this.valorDevolucaoPrevista)
                .taxaJuros(this.taxaJuros)
                .multaDiaria(this.multaDiaria)
                .periodoPagamento(this.periodoPagamento)
                .quantidadeParcelas(this.quantidadeParcelas)
                .valorParcela(this.valorParcela)
                .dataLiberacao(now)
                .dataPrevistaDevolucao(this.dataPrevistaDevolucao)
                .dataCriacao(this.dataCriacao)
                .dataAtualizacao(now)
                .status(LoanStatus.CREDITED)
                .build();
    }

    public Loan reject(Instant now) {
        return Loan.builder()
                .id(this.id)
                .customerId(this.customerId)
                .loanTypeId(this.loanTypeId)
                .valorSolicitado(this.valorSolicitado)
                .valorDevolucaoPrevista(this.valorDevolucaoPrevista)
                .taxaJuros(this.taxaJuros)
                .multaDiaria(this.multaDiaria)
                .periodoPagamento(this.periodoPagamento)
                .quantidadeParcelas(this.quantidadeParcelas)
                .valorParcela(this.valorParcela)
                .dataLiberacao(null)
                .dataPrevistaDevolucao(this.dataPrevistaDevolucao)
                .dataCriacao(this.dataCriacao)
                .dataAtualizacao(now)
                .status(LoanStatus.REJECTED)
                .build();
    }

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
                .loanTypeId(this.loanTypeId)
                .valorSolicitado(this.valorSolicitado)
                .valorDevolucaoPrevista(this.valorDevolucaoPrevista)
                .taxaJuros(this.taxaJuros)
                .multaDiaria(this.multaDiaria)
                .periodoPagamento(this.periodoPagamento)
                .quantidadeParcelas(this.quantidadeParcelas)
                .valorParcela(this.valorParcela)
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
                .loanTypeId(this.loanTypeId)
                .valorSolicitado(this.valorSolicitado)
                .valorDevolucaoPrevista(this.valorDevolucaoPrevista)
                .taxaJuros(this.taxaJuros)
                .multaDiaria(this.multaDiaria)
                .periodoPagamento(this.periodoPagamento)
                .quantidadeParcelas(this.quantidadeParcelas)
                .valorParcela(this.valorParcela)
                .dataLiberacao(this.dataLiberacao)
                .dataPrevistaDevolucao(this.dataPrevistaDevolucao)
                .dataCriacao(this.dataCriacao)
                .dataAtualizacao(Instant.now())
                .status(LoanStatus.PAID)
                .build();
    }

    public Loan applyRepaymentPlan(LoanPaymentPeriod periodoPagamento, Integer quantidadeParcelas, BigDecimal valorParcela) {
        return Loan.builder()
                .id(this.id)
                .customerId(this.customerId)
                .loanTypeId(this.loanTypeId)
                .valorSolicitado(this.valorSolicitado)
                .valorDevolucaoPrevista(this.valorDevolucaoPrevista)
                .taxaJuros(this.taxaJuros)
                .multaDiaria(this.multaDiaria)
                .periodoPagamento(periodoPagamento)
                .quantidadeParcelas(quantidadeParcelas)
                .valorParcela(valorParcela)
                .dataLiberacao(this.dataLiberacao)
                .dataPrevistaDevolucao(this.dataPrevistaDevolucao)
                .dataCriacao(this.dataCriacao)
                .dataAtualizacao(this.dataAtualizacao)
                .status(this.status)
                .build();
    }
}

