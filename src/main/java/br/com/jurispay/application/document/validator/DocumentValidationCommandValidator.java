package br.com.jurispay.application.document.validator;

import br.com.jurispay.application.common.validator.CommandValidator;
import br.com.jurispay.application.document.dto.DocumentValidationCommand;
import br.com.jurispay.domain.common.exception.ErrorCode;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.document.model.DocumentStatus;
import org.springframework.stereotype.Component;

/**
 * Validador para DocumentValidationCommand.
 * Extrai lógica de validação do use case.
 */
@Component
public class DocumentValidationCommandValidator implements CommandValidator<DocumentValidationCommand> {

    @Override
    public void validate(DocumentValidationCommand command) {
        if (command.getDocumentId() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "ID do documento é obrigatório.");
        }

        if (command.getStatus() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "Status de validação é obrigatório.");
        }

        if (command.getStatus() == DocumentStatus.UPLOADED) {
            throw new ValidationException(ErrorCode.INVALID_DOCUMENT_STATUS, "Status deve ser VALIDATED ou REJECTED.");
        }
    }
}

