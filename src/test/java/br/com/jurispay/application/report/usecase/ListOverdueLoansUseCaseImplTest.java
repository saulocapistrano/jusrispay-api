package br.com.jurispay.application.report.usecase;

import br.com.jurispay.application.report.dto.OverdueLoanItemResponse;
import br.com.jurispay.application.report.mapper.ReportApplicationMapper;
import br.com.jurispay.domain.report.model.OverdueLoanItem;
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
 * Testes para ListOverdueLoansUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
class ListOverdueLoansUseCaseImplTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ReportApplicationMapper mapper;

    @InjectMocks
    private ListOverdueLoansUseCaseImpl useCase;

    @Test
    void shouldUseDefaultLimitWhenLimitIsInvalid() {
        // TODO: Implementar teste de validação de limit
        // Given: limit <= 0 ou > 200
        // When: listar empréstimos em atraso
        // Then: usar limit = 20 (default)
    }

    @Test
    void shouldListOverdueLoansSuccessfully() {
        // TODO: Implementar teste de cenário feliz
        // Given: reportRepository retorna lista de OverdueLoanItem
        // When: listar empréstimos em atraso
        // Then: mapper converte e retorna lista de OverdueLoanItemResponse
    }
}

