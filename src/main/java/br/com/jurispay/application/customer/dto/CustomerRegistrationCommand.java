package br.com.jurispay.application.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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
    private String cpf;
    private String telefone;
    private String chavePix;
    private BigDecimal rendaMensal;
    private String ocupacaoAtual;
    private String redesSociais;
}

