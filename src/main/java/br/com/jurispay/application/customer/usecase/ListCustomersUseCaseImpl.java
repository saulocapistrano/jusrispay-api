package br.com.jurispay.application.customer.usecase;

import br.com.jurispay.application.customer.dto.CustomerResponse;
import br.com.jurispay.application.customer.mapper.CustomerApplicationMapper;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do caso de uso de listagem de clientes.
 */
@Service
public class ListCustomersUseCaseImpl implements ListCustomersUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerApplicationMapper mapper;

    public ListCustomersUseCaseImpl(
            CustomerRepository customerRepository,
            CustomerApplicationMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CustomerResponse> listAll() {
        return customerRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}

