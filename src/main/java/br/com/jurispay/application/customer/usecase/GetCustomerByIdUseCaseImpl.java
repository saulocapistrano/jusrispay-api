package br.com.jurispay.application.customer.usecase;

import br.com.jurispay.application.customer.dto.CustomerResponse;
import br.com.jurispay.application.customer.mapper.CustomerApplicationMapper;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.customer.model.Customer;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

/**
 * Implementação do caso de uso de busca de cliente por ID.
 */
@Service
public class GetCustomerByIdUseCaseImpl implements GetCustomerByIdUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerApplicationMapper mapper;

    public GetCustomerByIdUseCaseImpl(
            CustomerRepository customerRepository,
            CustomerApplicationMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    @Override
    public CustomerResponse getById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        return mapper.toResponse(customer);
    }
}

