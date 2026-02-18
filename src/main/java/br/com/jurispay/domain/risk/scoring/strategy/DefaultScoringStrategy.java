package br.com.jurispay.domain.risk.scoring.strategy;

import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanStatus;
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

public class DefaultScoringStrategy implements ScoringStrategy {

    private static final double BUREAU_WEIGHT = 0.40d;
    private static final double INTERNAL_HISTORY_WEIGHT = 0.30d;
    private static final double CURRENT_DEBT_WEIGHT = 0.20d;
    private static final double RELATIONSHIP_WEIGHT = 0.10d;

    @Override
    public RiskProfileType profileType() {
        return RiskProfileType.THICK_FILE;
    }

    @Override
    public ConfidenceLevel confidence() {
        return ConfidenceLevel.HIGH;
    }

    @Override
    public JurispayRiskAssessment calculate(ScoringContext context) {
        Integer bureauRaw = context.bureauScore();
        if (bureauRaw == null) {
            int score = 450;
            return new JurispayRiskAssessment(
                    ScoreModelVersion.V1,
                    profileType(),
                    ConfidenceLevel.MEDIUM,
                    score,
                    RiskBandClassifier.classify(score),
                    List.of(RiskSignal.BUREAU_UNAVAILABLE),
                    Map.of());
        }

        int bureauNormalized = ScoreNormalization.normalizeBureauScore(bureauRaw);
        int relationshipNormalized = relationshipScore(context.customer().getDataCriacao(), context.now());
        int internalHistoryNormalized = internalHistoryScore(context.customerLoans());
        int currentDebtNormalized = currentDebtScore(context.customerLoans());

        int bureauContribution = (int) Math.round(bureauNormalized * BUREAU_WEIGHT);
        int relationshipContribution = (int) Math.round(relationshipNormalized * RELATIONSHIP_WEIGHT);
        int internalHistoryContribution = (int) Math.round(internalHistoryNormalized * INTERNAL_HISTORY_WEIGHT);
        int currentDebtContribution = (int) Math.round(currentDebtNormalized * CURRENT_DEBT_WEIGHT);

        int finalScore = ScoreNormalization.clamp0To1000(
                bureauContribution + internalHistoryContribution + currentDebtContribution + relationshipContribution);

        Map<Pillar, PillarScore> pillars = new EnumMap<>(Pillar.class);
        pillars.put(Pillar.BUREAU, new PillarScore(bureauRaw, bureauNormalized, BUREAU_WEIGHT, bureauContribution));
        pillars.put(Pillar.INTERNAL_HISTORY, new PillarScore(null, internalHistoryNormalized, INTERNAL_HISTORY_WEIGHT, internalHistoryContribution));
        pillars.put(Pillar.CURRENT_DEBT, new PillarScore(null, currentDebtNormalized, CURRENT_DEBT_WEIGHT, currentDebtContribution));
        pillars.put(Pillar.RELATIONSHIP_TIME, new PillarScore(monthsBetween(context.customer().getDataCriacao(), context.now()), relationshipNormalized, RELATIONSHIP_WEIGHT, relationshipContribution));

        RiskBand band = RiskBandClassifier.classify(finalScore);
        return new JurispayRiskAssessment(
                ScoreModelVersion.V1,
                profileType(),
                confidence(),
                finalScore,
                band,
                List.of(),
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

    private int internalHistoryScore(List<Loan> loans) {
        if (loans == null || loans.isEmpty()) {
            return 500;
        }

        boolean hasOverdue = loans.stream().anyMatch(l -> l != null && l.getStatus() == LoanStatus.OVERDUE);
        boolean hasPaid = loans.stream().anyMatch(l -> l != null && l.getStatus() == LoanStatus.PAID);
        boolean hasRejected = loans.stream().anyMatch(l -> l != null && l.getStatus() == LoanStatus.REJECTED);

        int score = 700;
        if (hasPaid) {
            score += 200;
        }
        if (hasOverdue) {
            score -= 400;
        }
        if (hasRejected) {
            score -= 150;
        }

        return ScoreNormalization.clamp0To1000(score);
    }

    private int currentDebtScore(List<Loan> loans) {
        if (loans == null || loans.isEmpty()) {
            return 800;
        }

        boolean hasOverdue = loans.stream().anyMatch(l -> l != null && l.getStatus() == LoanStatus.OVERDUE);
        if (hasOverdue) {
            return 250;
        }

        boolean hasOpen = loans.stream().anyMatch(l -> l != null && l.getStatus() == LoanStatus.OPEN);
        if (hasOpen) {
            return 600;
        }

        return 900;
    }
}
