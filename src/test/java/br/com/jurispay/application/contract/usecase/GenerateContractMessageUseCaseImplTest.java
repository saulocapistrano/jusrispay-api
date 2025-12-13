package br.com.jurispay.application.contract.usecase;

import br.com.jurispay.application.contract.dto.ContractMessageResponse;
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
 * Testes para GenerateContractMessageUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class GenerateContractMessageUseCaseImplTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private GenerateContractMessageUseCaseImpl useCase;

    @Test
    void shouldThrowNotFoundExceptionWhenLoanDoesNotExist() {
        // TODO: Implementar teste de empréstimo não encontrado
        // Given: loanId não existe no repositório
        // When: gerar mensagem do contrato
        // Then: lançar NotFoundException("Empréstimo não encontrado.")
    }

    @Test
    void shouldGenerateMessageWithLoanDetails() {
        // TODO: Implementar teste de cenário feliz
        // Given: empréstimo existe com valorSolicitado e valorDevolucaoPrevista
        // When: gerar mensagem do contrato
        // Then: mensagem contém valorSolicitado e valorDevolucaoPrevista
    }
}

