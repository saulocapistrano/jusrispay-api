package br.com.jurispay.application.payment.usecase;

import br.com.jurispay.application.payment.dto.PaymentResponse;

import java.util.List;

/**
 * Caso de uso para listar pagamentos.
 */
public interface ListPaymentsUseCase {

    List<PaymentResponse> listAll();

    List<PaymentResponse> listByLoanId(Long loanId);
}

