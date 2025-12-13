package br.com.jurispay.application.collection.usecase;

import br.com.jurispay.application.collection.dto.CollectionActionCommand;
import br.com.jurispay.application.collection.dto.CollectionActionResponse;

/**
 * Caso de uso para registro de ação de cobrança.
 */
public interface RegisterCollectionActionUseCase {

    CollectionActionResponse register(CollectionActionCommand command);
}

