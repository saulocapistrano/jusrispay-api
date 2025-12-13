package br.com.jurispay.application.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Resposta com mensagem do contrato gerada.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractMessageResponse {

    private Long loanId;

    private Long customerId;

    private String message;

    private Instant generatedAt;
}

