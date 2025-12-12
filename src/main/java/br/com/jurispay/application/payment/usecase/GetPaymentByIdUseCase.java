package br.com.jurispay.application.payment.usecase;

import br.com.jurispay.application.payment.dto.PaymentResponse;

/**
 * Caso de uso para buscar pagamento por ID.
 */
public interface GetPaymentByIdUseCase {

    PaymentResponse getById(Long id);
}

