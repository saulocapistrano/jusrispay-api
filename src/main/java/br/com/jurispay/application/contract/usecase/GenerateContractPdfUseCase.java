package br.com.jurispay.application.contract.usecase;

import br.com.jurispay.application.contract.dto.ContractGenerationResponse;

/**
 * Caso de uso para gerar PDF do contrato de empréstimo.
 */
public interface GenerateContractPdfUseCase {

    ContractGenerationResponse generate(Long loanId);
}

