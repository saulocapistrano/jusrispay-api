package br.com.jurispay.api.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;

    private String chavePix;

    @NotNull(message = "Renda mensal é obrigatória")
    private BigDecimal rendaMensal;

    private String ocupacaoAtual;

    private String redesSociais;
}

