package br.com.jurispay.application.customer.usecase;

import br.com.jurispay.application.customer.dto.CustomerRegistrationCommand;
import br.com.jurispay.application.customer.dto.CustomerResponse;

/**
 * Caso de uso para registro de novo cliente.
 */
public interface RegisterCustomerUseCase {

    CustomerResponse register(CustomerRegistrationCommand command);
}

