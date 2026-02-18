package br.com.jurispay.application.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * DTO de resposta de cliente para a camada API.
 * CPF e chave PIX são representados apenas por campos mascarados (LGPD).
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private Long id;
    private String nomeCompleto;
    private Integer idade;
    private String cpfMascarado;
    private String rg;
    private LocalDate dataNasc;
    private String sexo;
    private String signo;
    private String mae;
    private String pai;
    private String email;
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
    private String telefone;
    private String chavePixMascarada;
    private BigDecimal rendaMensal;
    private String ocupacaoAtual;
    private String redesSociais;
    private Instant dataCriacao;
}

