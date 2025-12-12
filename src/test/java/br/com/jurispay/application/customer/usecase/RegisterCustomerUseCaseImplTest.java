package br.com.jurispay.application.customer.usecase;

import br.com.jurispay.application.customer.dto.CustomerRegistrationCommand;
import br.com.jurispay.application.customer.dto.CustomerResponse;
import br.com.jurispay.application.customer.mapper.CustomerApplicationMapper;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.customer.model.Customer;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes para RegisterCustomerUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class RegisterCustomerUseCaseImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerApplicationMapper mapper;

    @InjectMocks
    private RegisterCustomerUseCaseImpl useCase;

    @Test
    void shouldRegisterCustomerSuccessfully() {
        // TODO: Implementar teste de cenário feliz
        // Given: CPF não existe, comando válido
        // When: registrar cliente
        // Then: cliente salvo e retornado com sucesso
    }

    @Test
    void shouldThrowValidationExceptionWhenCpfAlreadyExists() {
        // TODO: Implementar teste de CPF duplicado
        // Given: CPF já existe no repositório
        // When: tentar registrar cliente com mesmo CPF
        // Then: lançar ValidationException com mensagem "CPF já cadastrado."
    }
}

