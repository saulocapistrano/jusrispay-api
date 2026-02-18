package br.com.jurispay.application.collection.usecase;

import br.com.jurispay.application.collection.dto.OverdueInfoResponse;

/**
 * Caso de uso para obter informações de inadimplência de um empréstimo.
 */
public interface GetOverdueInfoUseCase {

    OverdueInfoResponse getOverdueInfo(Long loanId);
}

