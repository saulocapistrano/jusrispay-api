package br.com.jurispay.application.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de comando para registro de novo cliente.
 * Representa os dados necessários para criar um cliente.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegistrationCommand {

    private String nomeCompleto;
    private Integer idade;
    private String cpf;
    private String rg;
    private LocalDate dataNasc;
    private String sexo;
    private String signo;
    private String mae;
    private String pai;
    private String email;
    private String senha;
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
    private String chavePix;
    private BigDecimal rendaMensal;
    private String ocupacaoAtual;
    private String redesSociais;
}

