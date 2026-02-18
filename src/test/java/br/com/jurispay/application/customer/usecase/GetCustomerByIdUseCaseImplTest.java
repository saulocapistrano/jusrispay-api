package br.com.jurispay.application.customer.usecase;

import br.com.jurispay.application.customer.mapper.CustomerApplicationMapper;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes para GetCustomerByIdUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class GetCustomerByIdUseCaseImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerApplicationMapper mapper;

    @InjectMocks
    private GetCustomerByIdUseCaseImpl useCase;

    @Test
    void shouldReturnCustomerWhenFound() {
        // TODO: Implementar teste de cliente encontrado
        // Given: ID existe no repositório
        // When: buscar por ID
        // Then: retornar CustomerResponse
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCustomerNotFound() {
        // TODO: Implementar teste de cliente não encontrado
        // Given: ID não existe no repositório
        // When: buscar por ID
        // Then: lançar NotFoundException com mensagem "Cliente não encontrado"
    }
}

