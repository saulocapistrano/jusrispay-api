package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.application.loan.mapper.LoanApplicationMapper;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Testes para GetLoanByIdUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class GetLoanByIdUseCaseImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanApplicationMapper mapper;

    @InjectMocks
    private GetLoanByIdUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        // Inicialização dos mocks já feita pelo MockitoExtension
    }

    @Test
    void shouldReturnLoanResponseWhenLoanExists() {
        // TODO: Implementar teste de empréstimo encontrado
        // Given: ID existe no repositório
        // When: buscar por ID
        // Then: retornar LoanResponse
    }

    @Test
    void shouldThrowNotFoundExceptionWhenLoanDoesNotExist() {
        // TODO: Implementar teste de empréstimo não encontrado
        // Given: ID não existe no repositório
        // When: buscar por ID
        // Then: lançar NotFoundException com mensagem "Empréstimo não encontrado."
    }
}

