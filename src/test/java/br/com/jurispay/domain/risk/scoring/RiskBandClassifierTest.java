package br.com.jurispay.domain.risk.scoring;

import br.com.jurispay.domain.risk.model.RiskBand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RiskBandClassifierTest {

    @Test
    void shouldClassifyRiskBandsByScore() {
        assertEquals(RiskBand.A, RiskBandClassifier.classify(800));
        assertEquals(RiskBand.A, RiskBandClassifier.classify(1000));

        assertEquals(RiskBand.B, RiskBandClassifier.classify(799));
        assertEquals(RiskBand.B, RiskBandClassifier.classify(650));

        assertEquals(RiskBand.C, RiskBandClassifier.classify(649));
        assertEquals(RiskBand.C, RiskBandClassifier.classify(500));

        assertEquals(RiskBand.D, RiskBandClassifier.classify(499));
        assertEquals(RiskBand.D, RiskBandClassifier.classify(350));

        assertEquals(RiskBand.E, RiskBandClassifier.classify(349));
        assertEquals(RiskBand.E, RiskBandClassifier.classify(0));
    }
}
