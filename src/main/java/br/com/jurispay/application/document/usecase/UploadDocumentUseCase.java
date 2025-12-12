package br.com.jurispay.application.document.usecase;

import br.com.jurispay.application.document.dto.DocumentResponse;
import br.com.jurispay.application.document.dto.DocumentUploadCommand;

/**
 * Use case para upload de documentos.
 */
public interface UploadDocumentUseCase {

    /**
     * Faz upload de um documento.
     *
     * @param command comando com dados do documento a ser enviado
     * @return resposta com informações do documento salvo
     */
    DocumentResponse upload(DocumentUploadCommand command);
}

