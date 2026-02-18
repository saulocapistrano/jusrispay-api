package br.com.jurispay.application.risk.usecase.impl;

import br.com.jurispay.domain.creditcheck.model.CreditCheck;
import br.com.jurispay.domain.creditcheck.model.CreditCheckDecision;
import br.com.jurispay.domain.creditcheck.model.CreditCheckStatus;
import br.com.jurispay.domain.creditcheck.repository.CreditCheckRepository;
import br.com.jurispay.domain.exception.common.NotFoundException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetLoanRiskAssessmentUseCaseImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private CreditCheckRepository creditCheckRepository;

    @InjectMocks
    private GetLoanRiskAssessmentUseCaseImpl useCase;

    @Test
    void shouldThrowNotFoundWhenLoanDoesNotExist() {
        when(loanRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.getByLoanId(10L));
    }

    @Test
    void shouldThrowNotFoundWhenCreditCheckDoesNotExist() {
        when(loanRepository.findById(10L)).thenReturn(Optional.of(Loan.builder().id(10L).build()));
        when(creditCheckRepository.findLatestByLoanId(10L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.getByLoanId(10L));
    }

    @Test
    void shouldReturnResponseWithNullAssessmentWhenNotPresentInJson() {
        Instant now = Instant.parse("2026-02-18T09:00:00Z");

        when(loanRepository.findById(10L)).thenReturn(Optional.of(Loan.builder().id(10L).build()));

        CreditCheck creditCheck = CreditCheck.builder()
                .id(99L)
                .loanId(10L)
                .providerName("FAKE")
                .status(CreditCheckStatus.COMPLETED)
                .decision(CreditCheckDecision.APPROVED)
                .summaryJson("{\"score\":720,\"decision\":\"APPROVED\"}")
                .finishedAt(now)
                .build();

        when(creditCheckRepository.findLatestByLoanId(10L)).thenReturn(Optional.of(creditCheck));

        var response = useCase.getByLoanId(10L);

        assertNotNull(response);
        assertEquals(10L, response.getLoanId());
        assertNotNull(response.getCreditCheck());
        assertEquals(720, response.getCreditCheck().getBureauScore());
        assertNull(response.getJurispayRiskAssessment());
    }

    @Test
    void shouldParseAssessmentWhenPresentInJson() {
        Instant now = Instant.parse("2026-02-18T09:00:00Z");

        when(loanRepository.findById(10L)).thenReturn(Optional.of(Loan.builder().id(10L).build()));

        String json = "{"
                + "\"score\":720,"
                + "\"jurispayRiskAssessment\":{"
                + "\"modelVersion\":\"V1\","
                + "\"profileType\":\"THIN_FILE\","
                + "\"confidence\":\"LOW\","
                + "\"finalScore\":576,"
                + "\"band\":\"C\","
                + "\"signals\":[\"THIN_FILE\"],"
                + "\"pillars\":{"
                + "\"BUREAU\":{\"raw\":720,\"normalized\":700,\"weight\":0.8,\"contribution\":560}"
                + "}"
                + "}"
                + "}";

        CreditCheck creditCheck = CreditCheck.builder()
                .id(99L)
                .loanId(10L)
                .providerName("FAKE")
                .status(CreditCheckStatus.COMPLETED)
                .decision(CreditCheckDecision.APPROVED)
                .summaryJson(json)
                .finishedAt(now)
                .build();

        when(creditCheckRepository.findLatestByLoanId(10L)).thenReturn(Optional.of(creditCheck));

        var response = useCase.getByLoanId(10L);

        assertNotNull(response);
        assertNotNull(response.getJurispayRiskAssessment());
        assertEquals("V1", response.getJurispayRiskAssessment().getModelVersion());
        assertEquals("THIN_FILE", response.getJurispayRiskAssessment().getProfileType());
        assertEquals("LOW", response.getJurispayRiskAssessment().getConfidence());
        assertEquals(576, response.getJurispayRiskAssessment().getFinalScore());
        assertEquals("C", response.getJurispayRiskAssessment().getBand());
        assertEquals(1, response.getJurispayRiskAssessment().getSignals().size());
        assertEquals(1, response.getJurispayRiskAssessment().getPillars().size());
        assertEquals("BUREAU", response.getJurispayRiskAssessment().getPillars().get(0).getPillar());
    }
}
