package br.com.jurispay.infrastructure.risk;

import br.com.jurispay.domain.risk.model.JurispayRiskAssessment;
import br.com.jurispay.domain.risk.model.RiskProfileType;
import br.com.jurispay.domain.risk.scoring.JurispayRiskAssessmentCalculator;
import br.com.jurispay.domain.risk.scoring.ScoringContext;
import br.com.jurispay.domain.risk.scoring.ScoringStrategy;
import br.com.jurispay.domain.risk.scoring.ScoringStrategySelector;
import br.com.jurispay.domain.risk.scoring.strategy.DefaultScoringStrategy;
import br.com.jurispay.domain.risk.scoring.strategy.ThinFileScoringStrategy;
import org.springframework.stereotype.Component;

@Component
public class JurispayRiskAssessmentCalculatorAdapter implements JurispayRiskAssessmentCalculator {

    private final ScoringStrategy thinFileStrategy;
    private final ScoringStrategy defaultStrategy;

    public JurispayRiskAssessmentCalculatorAdapter() {
        this.thinFileStrategy = new ThinFileScoringStrategy();
        this.defaultStrategy = new DefaultScoringStrategy();
    }

    @Override
    public JurispayRiskAssessment calculate(ScoringContext context) {
        RiskProfileType profileType = ScoringStrategySelector.resolveProfileType(context.bureauScore(), context.customerLoans());
        if (profileType == RiskProfileType.THIN_FILE) {
            return thinFileStrategy.calculate(context);
        }
        return defaultStrategy.calculate(context);
    }
}
