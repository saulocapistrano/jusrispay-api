package br.com.jurispay.application.creditanalysis.usecase;

import br.com.jurispay.application.creditanalysis.mapper.CreditAnalysisApplicationMapper;
import br.com.jurispay.domain.creditanalysis.repository.CreditAnalysisRepository;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

/**
 * Testes para StartCreditAnalysisUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class StartCreditAnalysisUseCaseImplTest {

    @Mock
    private CreditAnalysisRepository creditAnalysisRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CreditAnalysisApplicationMapper mapper;

    @InjectMocks
    private StartCreditAnalysisUseCaseImpl useCase;

    @Test
    void shouldStartAnalysisWhenCustomerExists() {
        // TODO: Implementar teste de cenário feliz
        // Given: cliente existe, análise não existe
        // When: iniciar análise
        // Then: análise criada com status IN_REVIEW e retornada com sucesso
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCustomerDoesNotExist() {
        // TODO: Implementar teste de cliente não encontrado
        // Given: customerId não existe no repositório
        // When: iniciar análise
        // Then: lançar NotFoundException("Cliente não encontrado para iniciar análise.")
    }

    @Test
    void shouldThrowValidationExceptionWhenAlreadyApproved() {
        // TODO: Implementar teste de cliente já aprovado
        // Given: cliente já possui análise com status APPROVED
        // When: tentar iniciar nova análise
        // Then: lançar ValidationException("Cliente já aprovado.")
    }
}

