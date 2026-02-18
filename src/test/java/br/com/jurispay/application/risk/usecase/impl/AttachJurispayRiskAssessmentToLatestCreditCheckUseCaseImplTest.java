package br.com.jurispay.application.risk.usecase.impl;

import br.com.jurispay.domain.creditcheck.model.CreditCheck;
import br.com.jurispay.domain.creditcheck.model.CreditCheckDecision;
import br.com.jurispay.domain.creditcheck.model.CreditCheckStatus;
import br.com.jurispay.domain.creditcheck.repository.CreditCheckRepository;
import br.com.jurispay.domain.customer.model.Customer;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import br.com.jurispay.domain.risk.model.ConfidenceLevel;
import br.com.jurispay.domain.risk.model.JurispayRiskAssessment;
import br.com.jurispay.domain.risk.model.RiskBand;
import br.com.jurispay.domain.risk.model.RiskProfileType;
import br.com.jurispay.domain.risk.model.ScoreModelVersion;
import br.com.jurispay.domain.risk.scoring.JurispayRiskAssessmentCalculator;
import br.com.jurispay.domain.risk.scoring.Pillar;
import br.com.jurispay.domain.risk.scoring.PillarScore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttachJurispayRiskAssessmentToLatestCreditCheckUseCaseImplTest {

    @Mock
    private CreditCheckRepository creditCheckRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private JurispayRiskAssessmentCalculator calculator;

    @InjectMocks
    private AttachJurispayRiskAssessmentToLatestCreditCheckUseCaseImpl useCase;

    @Captor
    private ArgumentCaptor<CreditCheck> creditCheckCaptor;

    @Test
    void shouldUpsertJurispayRiskAssessmentIntoSummaryJson() {
        Instant now = Instant.parse("2026-02-18T09:00:00Z");

        CreditCheck creditCheck = CreditCheck.builder()
                .id(99L)
                .loanId(10L)
                .customerId(1L)
                .cpf("000.000.000-00")
                .providerName("FAKE")
                .status(CreditCheckStatus.COMPLETED)
                .decision(CreditCheckDecision.APPROVED)
                .summaryJson("{\"score\":720,\"decision\":\"APPROVED\"}")
                .requestedByUserId(1L)
                .traceId("t")
                .createdAt(now)
                .finishedAt(now)
                .build();

        Loan loan = Loan.builder()
                .id(10L)
                .customerId(1L)
                .status(LoanStatus.REQUESTED)
                .build();

        Customer customer = Customer.builder()
                .id(1L)
                .cpf("000.000.000-00")
                .dataCriacao(now)
                .build();

        JurispayRiskAssessment assessment = new JurispayRiskAssessment(
                ScoreModelVersion.V1,
                RiskProfileType.THIN_FILE,
                ConfidenceLevel.LOW,
                600,
                RiskBand.C,
                List.of(),
                Map.of(Pillar.BUREAU, new PillarScore(720, 700, 0.8d, 560))
        );

        when(creditCheckRepository.findLatestByLoanId(10L)).thenReturn(Optional.of(creditCheck));
        when(loanRepository.findById(10L)).thenReturn(Optional.of(loan));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(loanRepository.findByCustomerId(1L)).thenReturn(List.of());
        when(calculator.calculate(any())).thenReturn(assessment);

        useCase.attach(10L);

        verify(creditCheckRepository).save(creditCheckCaptor.capture());
        CreditCheck saved = creditCheckCaptor.getValue();
        assertNotNull(saved);
        assertNotNull(saved.getSummaryJson());

        String json = saved.getSummaryJson();
        assertTrue(json.contains("\"score\":720"));
        assertTrue(json.contains("\"jurispayRiskAssessment\""));
        assertTrue(json.contains("\"finalScore\":600"));
        assertTrue(json.contains("\"modelVersion\":\"V1\""));
    }
}
