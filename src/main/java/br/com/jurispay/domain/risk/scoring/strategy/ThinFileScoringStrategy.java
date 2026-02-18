package br.com.jurispay.domain.risk.scoring.strategy;

import br.com.jurispay.domain.risk.model.ConfidenceLevel;
import br.com.jurispay.domain.risk.model.JurispayRiskAssessment;
import br.com.jurispay.domain.risk.model.RiskBand;
import br.com.jurispay.domain.risk.model.RiskProfileType;
import br.com.jurispay.domain.risk.model.RiskSignal;
import br.com.jurispay.domain.risk.model.ScoreModelVersion;
import br.com.jurispay.domain.risk.scoring.Pillar;
import br.com.jurispay.domain.risk.scoring.PillarScore;
import br.com.jurispay.domain.risk.scoring.RiskBandClassifier;
import br.com.jurispay.domain.risk.scoring.ScoreNormalization;
import br.com.jurispay.domain.risk.scoring.ScoringContext;
import br.com.jurispay.domain.risk.scoring.ScoringStrategy;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ThinFileScoringStrategy implements ScoringStrategy {

    private static final double BUREAU_WEIGHT = 0.80d;
    private static final double RELATIONSHIP_WEIGHT = 0.20d;
    private static final double CONFIDENCE_DISCOUNT = 0.90d;

    @Override
    public RiskProfileType profileType() {
        return RiskProfileType.THIN_FILE;
    }

    @Override
    public ConfidenceLevel confidence() {
        return ConfidenceLevel.LOW;
    }

    @Override
    public JurispayRiskAssessment calculate(ScoringContext context) {
        Integer bureauRaw = context.bureauScore();
        if (bureauRaw == null) {
            int score = 350;
            return new JurispayRiskAssessment(
                    ScoreModelVersion.V1,
                    profileType(),
                    confidence(),
                    score,
                    RiskBandClassifier.classify(score),
                    List.of(RiskSignal.THIN_FILE, RiskSignal.BUREAU_UNAVAILABLE),
                    Map.of());
        }

        int bureauNormalized = ScoreNormalization.normalizeBureauScore(bureauRaw);
        int relationshipNormalized = relationshipScore(context.customer().getDataCriacao(), context.now());

        int bureauContribution = (int) Math.round(bureauNormalized * BUREAU_WEIGHT);
        int relationshipContribution = (int) Math.round(relationshipNormalized * RELATIONSHIP_WEIGHT);

        int rawFinal = bureauContribution + relationshipContribution;
        int discounted = ScoreNormalization.clamp0To1000((int) Math.round(rawFinal * CONFIDENCE_DISCOUNT));

        Map<Pillar, PillarScore> pillars = new EnumMap<>(Pillar.class);
        pillars.put(Pillar.BUREAU, new PillarScore(bureauRaw, bureauNormalized, BUREAU_WEIGHT, bureauContribution));
        pillars.put(Pillar.RELATIONSHIP_TIME, new PillarScore(monthsBetween(context.customer().getDataCriacao(), context.now()), relationshipNormalized, RELATIONSHIP_WEIGHT, relationshipContribution));

        List<RiskSignal> signals = List.of(
                RiskSignal.THIN_FILE,
                RiskSignal.WEIGHT_REBALANCED,
                RiskSignal.CONFIDENCE_DISCOUNT_APPLIED
        );

        RiskBand band = RiskBandClassifier.classify(discounted);
        return new JurispayRiskAssessment(
                ScoreModelVersion.V1,
                profileType(),
                confidence(),
                discounted,
                band,
                signals,
                pillars);
    }

    private int relationshipScore(Instant createdAt, Instant now) {
        long months = monthsBetween(createdAt, now);
        if (months >= 24) {
            return 1000;
        }
        if (months >= 12) {
            return 800;
        }
        if (months >= 6) {
            return 600;
        }
        return 400;
    }

    private long monthsBetween(Instant createdAt, Instant now) {
        if (createdAt == null || now == null) {
            return 0;
        }
        LocalDate start = LocalDate.ofInstant(createdAt, ZoneOffset.UTC);
        LocalDate end = LocalDate.ofInstant(now, ZoneOffset.UTC);
        return ChronoUnit.MONTHS.between(start, end);
    }
}
