package br.com.jurispay.application.customer.usecase;

import br.com.jurispay.application.customer.dto.CustomerResponse;

import java.util.List;

/**
 * Caso de uso para listar todos os clientes.
 */
public interface ListCustomersUseCase {

    List<CustomerResponse> listAll();
}

