package br.com.jurispay.domain.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Entidade de domínio Customer.
 * Representa um cliente do sistema Jurispay.
 * Não contém anotações JPA - é domínio puro.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    private Long id;
    private String nomeCompleto;
    private String cpf;
    private String telefone;
    private String chavePix;
    private BigDecimal rendaMensal;
    private String ocupacaoAtual;
    private String redesSociais;
    private Instant dataCriacao;
    private Instant dataAtualizacao;
}

