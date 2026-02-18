package br.com.jurispay.domain.risk.scoring;

import br.com.jurispay.domain.risk.model.RiskBand;

public final class RiskBandClassifier {

    private RiskBandClassifier() {
    }

    public static RiskBand classify(int score) {
        if (score >= 800) {
            return RiskBand.A;
        }
        if (score >= 650) {
            return RiskBand.B;
        }
        if (score >= 500) {
            return RiskBand.C;
        }
        if (score >= 350) {
            return RiskBand.D;
        }
        return RiskBand.E;
    }
}
