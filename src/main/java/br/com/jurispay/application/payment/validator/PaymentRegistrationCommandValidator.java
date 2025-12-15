package br.com.jurispay.application.payment.validator;

import br.com.jurispay.application.common.validator.CommandValidator;
import br.com.jurispay.application.payment.dto.PaymentRegistrationCommand;
import br.com.jurispay.domain.common.exception.ErrorCode;
import br.com.jurispay.domain.common.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Validador para PaymentRegistrationCommand.
 * Extrai lógica de validação do use case.
 */
@Component
public class PaymentRegistrationCommandValidator implements CommandValidator<PaymentRegistrationCommand> {

    @Override
    public void validate(PaymentRegistrationCommand command) {
        if (command.getLoanId() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "ID do empréstimo é obrigatório.");
        }

        if (command.getValorPago() == null || command.getValorPago().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(ErrorCode.INVALID_AMOUNT, "Valor pago deve ser maior que zero.");
        }
    }
}

