package br.com.jurispay.api.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de requisição para criação de cliente.
 * Usado no endpoint POST /api/customers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {

    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;

    private Integer idade;

    @NotBlank(message = "CPF é obrigatório")
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

    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;

    private String chavePix;

    @NotNull(message = "Renda mensal é obrigatória")
    private BigDecimal rendaMensal;

    private String ocupacaoAtual;

    private String redesSociais;
}

