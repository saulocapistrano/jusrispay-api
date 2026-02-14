package br.com.jurispay.application.loan.validator;

import br.com.jurispay.application.common.validator.CommandValidator;
import br.com.jurispay.application.loan.dto.LoanCreationCommand;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.loan.model.LoanPaymentPeriod;
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

        if (command.getTaxaJuros() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "Taxa de juros é obrigatória.");
        }

        if (command.getTaxaJuros().compareTo(new BigDecimal("0.35")) < 0 || command.getTaxaJuros().compareTo(BigDecimal.ONE) > 0) {
            throw new ValidationException(ErrorCode.INVALID_VALUE, "Taxa de juros deve estar entre 35% e 100%.");
        }

        if (command.getPeriodoPagamento() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "Período de pagamento é obrigatório.");
        }

        if (command.getPeriodoPagamento() != LoanPaymentPeriod.DAILY) {
            throw new ValidationException(ErrorCode.INVALID_VALUE, "Somente o período DAILY está habilitado no momento.");
        }

        if (command.getDataPrevistaDevolucao() == null || command.getDataPrevistaDevolucao().isBefore(Instant.now())) {
            throw new ValidationException(ErrorCode.INVALID_DATE, "Data prevista de devolução deve ser futura.");
        }
    }
}

