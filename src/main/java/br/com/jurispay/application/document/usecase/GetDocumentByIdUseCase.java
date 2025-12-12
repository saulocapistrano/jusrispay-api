package br.com.jurispay.application.document.usecase;

import br.com.jurispay.application.document.dto.DocumentResponse;

/**
 * Use case para buscar documento por ID.
 */
public interface GetDocumentByIdUseCase {

    /**
     * Busca um documento por ID.
     *
     * @param id ID do documento
     * @return resposta com informações do documento
     */
    DocumentResponse getById(Long id);
}

