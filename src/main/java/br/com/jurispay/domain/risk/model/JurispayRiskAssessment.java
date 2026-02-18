package br.com.jurispay.domain.risk.model;

import br.com.jurispay.domain.risk.scoring.Pillar;
import br.com.jurispay.domain.risk.scoring.PillarScore;

import java.util.List;
import java.util.Map;

public record JurispayRiskAssessment(
        ScoreModelVersion modelVersion,
        RiskProfileType profileType,
        ConfidenceLevel confidence,
        int finalScore,
        RiskBand band,
        List<RiskSignal> signals,
        Map<Pillar, PillarScore> pillars
) {
}
