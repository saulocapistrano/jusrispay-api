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

    private static final BigDecimal MIN_INTEREST_RATE_DAILY = new BigDecimal("0.35");
    private static final BigDecimal MIN_INTEREST_RATE_NON_DAILY = new BigDecimal("0.08");

    @Override
    public void validate(LoanCreationCommand command) {
        if (command.getValorSolicitado() == null || command.getValorSolicitado().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(ErrorCode.INVALID_AMOUNT, "Valor solicitado deve ser maior que zero.");
        }

        if (command.getLoanTypeId() == null || command.getLoanTypeId() <= 0) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "Tipo de empréstimo é obrigatório.");
        }

        if (command.getTaxaJuros() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "Taxa de juros é obrigatória.");
        }

        LoanPaymentPeriod periodoPagamento = command.getPeriodoPagamento();
        boolean isDaily = periodoPagamento == null || periodoPagamento == LoanPaymentPeriod.DAILY;

        BigDecimal minRate = isDaily ? MIN_INTEREST_RATE_DAILY : MIN_INTEREST_RATE_NON_DAILY;
        if (command.getTaxaJuros().compareTo(minRate) < 0 || command.getTaxaJuros().compareTo(BigDecimal.ONE) > 0) {
            String minPercent = isDaily ? "35%" : "8%";
            throw new ValidationException(ErrorCode.INVALID_VALUE, "Taxa de juros deve estar entre " + minPercent + " e 100%.");
        }

        if (command.getDataPrevistaDevolucao() == null || command.getDataPrevistaDevolucao().isBefore(Instant.now())) {
            throw new ValidationException(ErrorCode.INVALID_DATE, "Data prevista de devolução deve ser futura.");
        }
    }
}

