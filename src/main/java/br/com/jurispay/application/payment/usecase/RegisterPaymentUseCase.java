package br.com.jurispay.application.payment.usecase;

import br.com.jurispay.application.payment.dto.PaymentRegistrationCommand;
import br.com.jurispay.application.payment.dto.PaymentResponse;

/**
 * Caso de uso para registro de novo pagamento.
 */
public interface RegisterPaymentUseCase {

    PaymentResponse register(PaymentRegistrationCommand command);
}

