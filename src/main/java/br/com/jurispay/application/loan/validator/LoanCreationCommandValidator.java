package br.com.jurispay.application.loan.validator;

import br.com.jurispay.application.common.validator.CommandValidator;
import br.com.jurispay.application.loan.dto.LoanCreationCommand;
import br.com.jurispay.domain.common.exception.ErrorCode;
import br.com.jurispay.domain.common.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Validador para LoanCreationCommand.
 * Extrai lógica de validação do use case.
 */
@Component
public class LoanCreationCommandValidator implements CommandValidator<LoanCreationCommand> {

    @Override
    public void validate(LoanCreationCommand command) {
        if (command.getValorSolicitado() == null || command.getValorSolicitado().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(ErrorCode.INVALID_AMOUNT, "Valor solicitado deve ser maior que zero.");
        }

        if (command.getDataPrevistaDevolucao() == null || command.getDataPrevistaDevolucao().isBefore(Instant.now())) {
            throw new ValidationException(ErrorCode.INVALID_DATE, "Data prevista de devolução deve ser futura.");
        }
    }
}

