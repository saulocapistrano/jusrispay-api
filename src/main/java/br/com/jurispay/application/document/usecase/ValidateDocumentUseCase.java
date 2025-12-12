package br.com.jurispay.application.document.usecase;

import br.com.jurispay.application.document.dto.DocumentResponse;
import br.com.jurispay.application.document.dto.DocumentValidationCommand;

/**
 * Use case para validar/rejeitar documentos.
 */
public interface ValidateDocumentUseCase {

    /**
     * Valida ou rejeita um documento.
     *
     * @param command comando com informações da validação
     * @return resposta com informações do documento atualizado
     */
    DocumentResponse validate(DocumentValidationCommand command);
}

