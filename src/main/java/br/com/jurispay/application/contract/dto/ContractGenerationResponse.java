package br.com.jurispay.application.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Resposta com informações do contrato PDF gerado.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractGenerationResponse {

    private Long loanId;

    private Long customerId;

    private Long documentId;

    private String fileName;

    private Instant generatedAt;
}

