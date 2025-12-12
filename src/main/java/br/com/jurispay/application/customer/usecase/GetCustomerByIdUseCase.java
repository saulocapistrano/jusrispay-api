package br.com.jurispay.application.customer.usecase;

import br.com.jurispay.application.customer.dto.CustomerResponse;

/**
 * Caso de uso para buscar cliente por ID.
 */
public interface GetCustomerByIdUseCase {

    CustomerResponse getById(Long id);
}

