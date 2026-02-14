package br.com.jurispay.infrastructure.persistence.jpa.entity;

import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.model.LoanPaymentPeriod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entidade JPA para Loan.
 * Representa a tabela loan no banco de dados.
 */
@Entity
@Table(name = "loan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorSolicitado;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorDevolucaoPrevista;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal taxaJuros;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal multaDiaria;

    @Enumerated(EnumType.STRING)
    @Column(name = "periodo_pagamento", nullable = true, length = 20)
    private LoanPaymentPeriod periodoPagamento;

    @Column(name = "quantidade_parcelas", nullable = true)
    private Integer quantidadeParcelas;

    @Column(name = "valor_parcela", nullable = true, precision = 19, scale = 2)
    private BigDecimal valorParcela;

    @Column(nullable = true)
    private Instant dataLiberacao;

    @Column(nullable = false)
    private Instant dataPrevistaDevolucao;

    @Column(nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(nullable = false)
    private Instant dataAtualizacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LoanStatus status;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        if (dataCriacao == null) {
            dataCriacao = now;
        }
        dataAtualizacao = now;
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = Instant.now();
    }
}

