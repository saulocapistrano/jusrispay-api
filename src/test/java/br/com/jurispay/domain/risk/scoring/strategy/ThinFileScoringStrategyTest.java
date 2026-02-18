package br.com.jurispay.domain.risk.scoring.strategy;

import br.com.jurispay.domain.customer.model.Customer;
import br.com.jurispay.domain.risk.model.ConfidenceLevel;
import br.com.jurispay.domain.risk.model.JurispayRiskAssessment;
import br.com.jurispay.domain.risk.model.RiskBand;
import br.com.jurispay.domain.risk.model.RiskProfileType;
import br.com.jurispay.domain.risk.model.RiskSignal;
import br.com.jurispay.domain.risk.model.ScoreModelVersion;
import br.com.jurispay.domain.risk.scoring.Pillar;
import br.com.jurispay.domain.risk.scoring.PillarScore;
import br.com.jurispay.domain.risk.scoring.ScoringContext;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ThinFileScoringStrategyTest {

    @Test
    void shouldCalculateThinFileScoreWithRebalancedWeightsAndDiscount() {
        Instant now = Instant.parse("2026-02-18T09:00:00Z");
        Instant createdAt = LocalDate.ofInstant(now, ZoneOffset.UTC)
                .minusMonths(1)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();

        Customer customer = Customer.builder()
                .id(1L)
                .dataCriacao(createdAt)
                .build();

        ScoringContext context = new ScoringContext(
                10L,
                customer,
                720,
                List.of(),
                now);

        ThinFileScoringStrategy strategy = new ThinFileScoringStrategy();
        JurispayRiskAssessment assessment = strategy.calculate(context);

        assertEquals(ScoreModelVersion.V1, assessment.modelVersion());
        assertEquals(RiskProfileType.THIN_FILE, assessment.profileType());
        assertEquals(ConfidenceLevel.LOW, assessment.confidence());

        assertEquals(576, assessment.finalScore());
        assertEquals(RiskBand.C, assessment.band());

        assertTrue(assessment.signals().contains(RiskSignal.THIN_FILE));
        assertTrue(assessment.signals().contains(RiskSignal.WEIGHT_REBALANCED));
        assertTrue(assessment.signals().contains(RiskSignal.CONFIDENCE_DISCOUNT_APPLIED));

        PillarScore bureau = assessment.pillars().get(Pillar.BUREAU);
        assertNotNull(bureau);
        assertEquals(720, bureau.raw());
        assertEquals(700, bureau.normalized());
        assertEquals(0.80d, bureau.weight(), 0.000001d);
        assertEquals(560, bureau.contribution());

        PillarScore relationship = assessment.pillars().get(Pillar.RELATIONSHIP_TIME);
        assertNotNull(relationship);
        assertEquals(400, relationship.normalized());
        assertEquals(0.20d, relationship.weight(), 0.000001d);
        assertEquals(80, relationship.contribution());
    }

    @Test
    void shouldFallbackWhenBureauScoreIsMissing() {
        Instant now = Instant.parse("2026-02-18T09:00:00Z");

        Customer customer = Customer.builder()
                .id(1L)
                .dataCriacao(now)
                .build();

        ScoringContext context = new ScoringContext(
                10L,
                customer,
                null,
                List.of(),
                now);

        ThinFileScoringStrategy strategy = new ThinFileScoringStrategy();
        JurispayRiskAssessment assessment = strategy.calculate(context);

        assertEquals(RiskProfileType.THIN_FILE, assessment.profileType());
        assertEquals(350, assessment.finalScore());
        assertEquals(RiskBand.D, assessment.band());
        assertTrue(assessment.signals().contains(RiskSignal.BUREAU_UNAVAILABLE));
    }
}
