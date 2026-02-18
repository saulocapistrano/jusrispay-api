package br.com.jurispay.application.contract.usecase;

import br.com.jurispay.application.contract.dto.ContractMessageResponse;

/**
 * Caso de uso para gerar mensagem do contrato de empréstimo.
 */
public interface GenerateContractMessageUseCase {

    ContractMessageResponse generate(Long loanId);
}

