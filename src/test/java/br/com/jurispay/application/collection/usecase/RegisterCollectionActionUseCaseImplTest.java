package br.com.jurispay.application.collection.usecase;

import br.com.jurispay.application.collection.dto.CollectionActionCommand;
import br.com.jurispay.application.collection.dto.CollectionActionResponse;
import br.com.jurispay.application.collection.mapper.CollectionApplicationMapper;
import br.com.jurispay.domain.collection.model.CollectionAction;
import br.com.jurispay.domain.collection.model.CollectionChannel;
import br.com.jurispay.domain.collection.repository.CollectionRepository;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes para RegisterCollectionActionUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class RegisterCollectionActionUseCaseImplTest {

    @Mock
    private CollectionRepository collectionRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private CollectionApplicationMapper mapper;

    @InjectMocks
    private RegisterCollectionActionUseCaseImpl useCase;

    @Test
    void shouldThrowNotFoundExceptionWhenLoanDoesNotExist() {
        // TODO: Implementar teste de empréstimo não encontrado
        // Given: loanId não existe no repositório
        // When: registrar ação de cobrança
        // Then: lançar NotFoundException("Empréstimo não encontrado para registro de cobrança.")
    }

    @Test
    void shouldThrowValidationExceptionWhenSummaryExceedsMaxLength() {
        // TODO: Implementar teste de summary > 300 caracteres
        // Given: comando com summary > 300 caracteres
        // When: registrar ação de cobrança
        // Then: lançar ValidationException("Resumo da ação não pode exceder 300 caracteres.")
    }

    @Test
    void shouldRegisterActionSuccessfully() {
        // TODO: Implementar teste de cenário feliz
        // Given: empréstimo existe, comando válido
        // When: registrar ação de cobrança
        // Then: ação salva e retornada com sucesso
    }
}

