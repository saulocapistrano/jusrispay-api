package br.com.jurispay.domain.risk.scoring.strategy;

import br.com.jurispay.domain.customer.model.Customer;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.risk.model.ConfidenceLevel;
import br.com.jurispay.domain.risk.model.JurispayRiskAssessment;
import br.com.jurispay.domain.risk.model.RiskProfileType;
import br.com.jurispay.domain.risk.model.ScoreModelVersion;
import br.com.jurispay.domain.risk.scoring.Pillar;
import br.com.jurispay.domain.risk.scoring.ScoringContext;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultScoringStrategyTest {

    @Test
    void shouldCalculateDefaultScoreWithAllPillars() {
        Instant now = Instant.parse("2026-02-18T09:00:00Z");
        Instant createdAt = LocalDate.ofInstant(now, ZoneOffset.UTC)
                .minusMonths(18)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();

        Customer customer = Customer.builder()
                .id(1L)
                .dataCriacao(createdAt)
                .build();

        List<Loan> loans = List.of(
                Loan.builder().status(LoanStatus.PAID).build(),
                Loan.builder().status(LoanStatus.OPEN).build()
        );

        ScoringContext context = new ScoringContext(
                10L,
                customer,
                720,
                loans,
                now);

        DefaultScoringStrategy strategy = new DefaultScoringStrategy();
        JurispayRiskAssessment assessment = strategy.calculate(context);

        assertEquals(ScoreModelVersion.V1, assessment.modelVersion());
        assertEquals(RiskProfileType.THICK_FILE, assessment.profileType());
        assertEquals(ConfidenceLevel.HIGH, assessment.confidence());

        assertNotNull(assessment.pillars().get(Pillar.BUREAU));
        assertNotNull(assessment.pillars().get(Pillar.INTERNAL_HISTORY));
        assertNotNull(assessment.pillars().get(Pillar.CURRENT_DEBT));
        assertNotNull(assessment.pillars().get(Pillar.RELATIONSHIP_TIME));

        assertTrue(assessment.finalScore() >= 0);
        assertTrue(assessment.finalScore() <= 1000);
    }

    @Test
    void shouldPenalizeOverdueLoansInDefaultStrategy() {
        Instant now = Instant.parse("2026-02-18T09:00:00Z");

        Customer customer = Customer.builder()
                .id(1L)
                .dataCriacao(LocalDate.ofInstant(now, ZoneOffset.UTC)
                        .minusMonths(30)
                        .atStartOfDay(ZoneOffset.UTC)
                        .toInstant())
                .build();

        ScoringContext contextOk = new ScoringContext(
                10L,
                customer,
                720,
                List.of(Loan.builder().status(LoanStatus.PAID).build()),
                now);

        ScoringContext contextOverdue = new ScoringContext(
                10L,
                customer,
                720,
                List.of(Loan.builder().status(LoanStatus.OVERDUE).build()),
                now);

        DefaultScoringStrategy strategy = new DefaultScoringStrategy();

        int scoreOk = strategy.calculate(contextOk).finalScore();
        int scoreOverdue = strategy.calculate(contextOverdue).finalScore();

        assertTrue(scoreOverdue < scoreOk);
    }
}
