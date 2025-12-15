package br.com.jurispay.domain.loan.factory;

import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.policy.LoanPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para LoanFactory.
 */
class LoanFactoryTest {

    private LoanFactory loanFactory;
    private LoanPolicy loanPolicy;

    @BeforeEach
    void setUp() {
        loanFactory = new LoanFactory();
        loanPolicy = mock(LoanPolicy.class);
    }

    @Test
    void shouldCreateLoanWithCorrectValues() {
        // Given
        BigDecimal taxaJuros = new BigDecimal("0.30");
        BigDecimal multaDiaria = new BigDecimal("20.00");
        BigDecimal valorSolicitado = new BigDecimal("1000.00");
        Long customerId = 1L;
        Instant dataPrevistaDevolucao = Instant.now().plusSeconds(86400); // 1 dia no futuro
        Instant now = Instant.now();

        when(loanPolicy.getInterestRate()).thenReturn(taxaJuros);
        when(loanPolicy.getDailyFine()).thenReturn(multaDiaria);

        LoanCreationData data = LoanCreationData.builder()
                .customerId(customerId)
                .valorSolicitado(valorSolicitado)
                .dataPrevistaDevolucao(dataPrevistaDevolucao)
                .build();

        // When
        Loan loan = loanFactory.create(data, loanPolicy, now);

        // Then
        assertNotNull(loan);
        assertEquals(customerId, loan.getCustomerId());
        assertEquals(valorSolicitado, loan.getValorSolicitado());
        assertEquals(dataPrevistaDevolucao, loan.getDataPrevistaDevolucao());
        assertEquals(taxaJuros, loan.getTaxaJuros());
        assertEquals(multaDiaria, loan.getMultaDiaria());
        assertEquals(LoanStatus.OPEN, loan.getStatus());
        assertEquals(now, loan.getDataLiberacao());
        assertEquals(now, loan.getDataCriacao());
        assertEquals(now, loan.getDataAtualizacao());

        // Verificar cálculo de valorDevolucaoPrevista: valorSolicitado * (1 + taxaJuros)
        BigDecimal expectedValorDevolucao = valorSolicitado.multiply(BigDecimal.ONE.add(taxaJuros));
        assertEquals(expectedValorDevolucao, loan.getValorDevolucaoPrevista());
    }

    @Test
    void shouldCalculateValorDevolucaoPrevistaCorrectly() {
        // Given
        BigDecimal taxaJuros = new BigDecimal("0.30");
        BigDecimal valorSolicitado = new BigDecimal("5000.00");
        BigDecimal expectedValorDevolucao = new BigDecimal("6500.00"); // 5000 * 1.30

        when(loanPolicy.getInterestRate()).thenReturn(taxaJuros);
        when(loanPolicy.getDailyFine()).thenReturn(new BigDecimal("20.00"));

        LoanCreationData data = LoanCreationData.builder()
                .customerId(1L)
                .valorSolicitado(valorSolicitado)
                .dataPrevistaDevolucao(Instant.now().plusSeconds(86400))
                .build();

        // When
        Loan loan = loanFactory.create(data, loanPolicy, Instant.now());

        // Then
        assertEquals(0, expectedValorDevolucao.compareTo(loan.getValorDevolucaoPrevista()));
    }

    @Test
    void shouldSetStatusAsOpen() {
        // Given
        when(loanPolicy.getInterestRate()).thenReturn(new BigDecimal("0.30"));
        when(loanPolicy.getDailyFine()).thenReturn(new BigDecimal("20.00"));

        LoanCreationData data = LoanCreationData.builder()
                .customerId(1L)
                .valorSolicitado(new BigDecimal("1000.00"))
                .dataPrevistaDevolucao(Instant.now().plusSeconds(86400))
                .build();

        // When
        Loan loan = loanFactory.create(data, loanPolicy, Instant.now());

        // Then
        assertEquals(LoanStatus.OPEN, loan.getStatus());
    }

    @Test
    void shouldUsePolicyValuesForTaxaAndMulta() {
        // Given
        BigDecimal customTaxa = new BigDecimal("0.25");
        BigDecimal customMulta = new BigDecimal("15.00");

        when(loanPolicy.getInterestRate()).thenReturn(customTaxa);
        when(loanPolicy.getDailyFine()).thenReturn(customMulta);

        LoanCreationData data = LoanCreationData.builder()
                .customerId(1L)
                .valorSolicitado(new BigDecimal("1000.00"))
                .dataPrevistaDevolucao(Instant.now().plusSeconds(86400))
                .build();

        // When
        Loan loan = loanFactory.create(data, loanPolicy, Instant.now());

        // Then
        assertEquals(customTaxa, loan.getTaxaJuros());
        assertEquals(customMulta, loan.getMultaDiaria());
    }
}

