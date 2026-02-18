package br.com.jurispay.application.collection.usecase;

import br.com.jurispay.domain.collection.service.OverdueCalculator;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

/**
 * Testes para GetOverdueInfoUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class GetOverdueInfoUseCaseImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private OverdueCalculator overdueCalculator;

    @InjectMocks
    private GetOverdueInfoUseCaseImpl useCase;

    @Test
    void shouldThrowNotFoundExceptionWhenLoanDoesNotExist() {
        // TODO: Implementar teste de empréstimo não encontrado
        // Given: loanId não existe no repositório
        // When: obter informações de inadimplência
        // Then: lançar NotFoundException("Empréstimo não encontrado.")
    }

    @Test
    void shouldCalculateOverdueInfoWhenLoanIsOverdue() {
        // TODO: Implementar teste de cálculo de inadimplência
        // Given: empréstimo existe, dueDate no passado, multa diária definida
        // When: obter informações de inadimplência
        // Then: daysOverdue > 0, totalFine calculado corretamente
        // Nota: pode mockar OverdueCalculator para retornar valores esperados
    }
}

