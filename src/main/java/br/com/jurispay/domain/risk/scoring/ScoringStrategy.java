package br.com.jurispay.domain.risk.scoring;

import br.com.jurispay.domain.risk.model.ConfidenceLevel;
import br.com.jurispay.domain.risk.model.JurispayRiskAssessment;
import br.com.jurispay.domain.risk.model.RiskProfileType;

public interface ScoringStrategy {

    RiskProfileType profileType();

    ConfidenceLevel confidence();

    JurispayRiskAssessment calculate(ScoringContext context);
}
