package br.com.jurispay.domain.risk.scoring;

import br.com.jurispay.domain.risk.model.JurispayRiskAssessment;

public interface JurispayRiskAssessmentCalculator {

    JurispayRiskAssessment calculate(ScoringContext context);
}
