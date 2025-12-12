package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.application.loan.mapper.LoanApplicationMapper;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes para CreateLoanUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class CreateLoanUseCaseImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LoanApplicationMapper loanApplicationMapper;

    @InjectMocks
    private CreateLoanUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        // Inicialização dos mocks já feita pelo MockitoExtension
    }

    @Test
    void shouldCreateLoanWhenValidData() {
        // TODO: Implementar teste de cenário feliz
        // Given: comando válido, cliente existe
        // When: criar empréstimo
        // Then: empréstimo salvo e retornado com sucesso
    }

    @Test
    void shouldThrowValidationExceptionWhenValorSolicitadoIsZeroOrNegative() {
        // TODO: Implementar teste de validação de valor
        // Given: valorSolicitado <= 0
        // When: criar empréstimo
        // Then: lançar ValidationException
    }

    @Test
    void shouldThrowValidationExceptionWhenDataPrevistaIsPast() {
        // TODO: Implementar teste de validação de data
        // Given: dataPrevistaDevolucao no passado
        // When: criar empréstimo
        // Then: lançar ValidationException
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCustomerDoesNotExist() {
        // TODO: Implementar teste de cliente não encontrado
        // Given: customerId não existe no repositório
        // When: criar empréstimo
        // Then: lançar NotFoundException
    }
}

