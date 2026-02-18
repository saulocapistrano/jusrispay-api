package br.com.jurispay.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "loan_type", uniqueConstraints = {
        @UniqueConstraint(name = "uk_loan_type_nome", columnNames = {"nome"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(nullable = true, length = 255)
    private String descricao;

    @Column(name = "intervalo_pagamento_dias", nullable = false)
    private Integer intervaloPagamentoDias;

    @Column(name = "schedule_type", nullable = false, length = 30)
    private String scheduleType;

    @Column(name = "weekly_day_of_week", nullable = true, length = 10)
    private String weeklyDayOfWeek;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(nullable = false)
    private Instant dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        if (dataCriacao == null) {
            dataCriacao = now;
        }
        if (dataAtualizacao == null) {
            dataAtualizacao = now;
        }
        if (ativo == null) {
            ativo = Boolean.TRUE;
        }
        if (scheduleType == null) {
            scheduleType = "INTERVAL_DAYS";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = Instant.now();
        if (ativo == null) {
            ativo = Boolean.TRUE;
        }
        if (scheduleType == null) {
            scheduleType = "INTERVAL_DAYS";
        }
    }
}
