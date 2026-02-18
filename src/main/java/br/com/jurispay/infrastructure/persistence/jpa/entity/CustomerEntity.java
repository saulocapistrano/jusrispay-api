package br.com.jurispay.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Entidade JPA para Customer.
 * Representa a tabela customer no banco de dados.
 */
@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeCompleto;

    private Integer idade;

    @Column(nullable = false, unique = true)
    private String cpf;

    private String rg;

    private LocalDate dataNasc;

    private String sexo;

    private String signo;

    private String mae;

    private String pai;

    private String email;

    private String senhaHash;

    private String cep;

    private String endereco;

    private Integer numero;

    private String bairro;

    private String cidade;

    private String estado;

    private String telefoneFixo;

    private String celular;

    private Boolean bemGarantidor;

    private String descricaoBem;

    @Column(nullable = false)
    private String telefone;

    private String chavePix;

    @Column(nullable = false)
    private BigDecimal rendaMensal;

    private String ocupacaoAtual;

    private String redesSociais;

    @Column(nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(nullable = false)
    private Instant dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        dataCriacao = now;
        dataAtualizacao = now;
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = Instant.now();
    }
}

