package br.com.jurispay.application.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

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
    private String cpfMascarado;
    private String telefone;
    private String chavePixMascarada;
    private BigDecimal rendaMensal;
    private String ocupacaoAtual;
    private String redesSociais;
    private Instant dataCriacao;
}

