package br.com.jurispay.application.customer.usecase;

import br.com.jurispay.application.customer.dto.CustomerRegistrationCommand;
import br.com.jurispay.application.customer.dto.CustomerResponse;
import br.com.jurispay.application.customer.mapper.CustomerApplicationMapper;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.customer.model.Customer;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

/**
 * Implementação do caso de uso de registro de cliente.
 */
@Service
public class RegisterCustomerUseCaseImpl implements RegisterCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerApplicationMapper mapper;

    public RegisterCustomerUseCaseImpl(
            CustomerRepository customerRepository,
            CustomerApplicationMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    @Override
    public CustomerResponse register(CustomerRegistrationCommand command) {
        // Valida se CPF já existe
        if (customerRepository.existsByCpf(command.getCpf())) {
            throw new ValidationException("CPF já cadastrado.");
        }

        // Converte comando para domínio
        Customer customer = mapper.toDomain(command);

        // Salva cliente
        Customer savedCustomer = customerRepository.save(customer);

        // Retorna resposta mascarada
        return mapper.toResponse(savedCustomer);
    }
}

