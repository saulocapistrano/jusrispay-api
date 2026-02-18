package br.com.jurispay.domain.risk.scoring;

import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.risk.model.RiskProfileType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoringStrategySelectorTest {

    @Test
    void shouldResolveNoHitWhenNoBureauAndNoHistory() {
        RiskProfileType type = ScoringStrategySelector.resolveProfileType(null, List.of());
        assertEquals(RiskProfileType.NO_HIT, type);
    }

    @Test
    void shouldResolveThinFileWhenHasBureauButNoHistory() {
        RiskProfileType type = ScoringStrategySelector.resolveProfileType(720, List.of());
        assertEquals(RiskProfileType.THIN_FILE, type);
    }

    @Test
    void shouldResolveThickFileWhenHasHistory() {
        Loan loan = Loan.builder().status(LoanStatus.PAID).build();
        RiskProfileType type = ScoringStrategySelector.resolveProfileType(720, List.of(loan));
        assertEquals(RiskProfileType.THICK_FILE, type);
    }
}
