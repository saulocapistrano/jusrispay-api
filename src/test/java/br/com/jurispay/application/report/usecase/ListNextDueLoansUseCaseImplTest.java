package br.com.jurispay.application.report.usecase;

import br.com.jurispay.application.report.dto.DueLoanItemResponse;
import br.com.jurispay.application.report.mapper.ReportApplicationMapper;
import br.com.jurispay.domain.report.model.DueLoanItem;
import br.com.jurispay.domain.report.repository.ReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes para ListNextDueLoansUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class ListNextDueLoansUseCaseImplTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ReportApplicationMapper mapper;

    @InjectMocks
    private ListNextDueLoansUseCaseImpl useCase;

    @Test
    void shouldUseDefaultLimitWhenLimitIsInvalid() {
        // TODO: Implementar teste de validação de limit
        // Given: limit <= 0 ou > 200
        // When: listar próximos vencimentos
        // Then: usar limit = 20 (default)
    }

    @Test
    void shouldListNextDueLoansSuccessfully() {
        // TODO: Implementar teste de cenário feliz
        // Given: reportRepository retorna lista de DueLoanItem
        // When: listar próximos vencimentos
        // Then: mapper converte e retorna lista de DueLoanItemResponse
    }
}

