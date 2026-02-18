package br.com.jurispay.application.collection.usecase;

import br.com.jurispay.application.collection.dto.CollectionActionResponse;

import java.util.List;

/**
 * Caso de uso para listar ações de cobrança por empréstimo.
 */
public interface ListCollectionActionsByLoanUseCase {

    List<CollectionActionResponse> listByLoanId(Long loanId);
}

