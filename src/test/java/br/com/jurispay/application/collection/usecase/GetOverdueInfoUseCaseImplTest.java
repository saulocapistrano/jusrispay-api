package br.com.jurispay.application.collection.usecase;

import br.com.jurispay.application.collection.dto.OverdueInfoResponse;
import br.com.jurispay.domain.collection.model.OverdueInfo;
import br.com.jurispay.domain.collection.service.OverdueCalculator;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

