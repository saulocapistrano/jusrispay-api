package br.com.jurispay.application.creditanalysis.validator;

import br.com.jurispay.application.common.validator.CommandValidator;
import br.com.jurispay.application.creditanalysis.dto.StartCreditAnalysisCommand;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.ValidationException;
import org.springframework.stereotype.Component;

/**
 * Validador para StartCreditAnalysisCommand.
 * Extrai lógica de validação do use case.
 */
@Component
public class StartCreditAnalysisCommandValidator implements CommandValidator<StartCreditAnalysisCommand> {

    @Override
    public void validate(StartCreditAnalysisCommand command) {
        if (command.getLoanId() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "ID do empréstimo é obrigatório.");
        }

        if (command.getAnalystUserId() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "ID do analista é obrigatório.");
        }
    }
}

