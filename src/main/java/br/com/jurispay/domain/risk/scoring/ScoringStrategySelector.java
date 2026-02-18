package br.com.jurispay.domain.risk.scoring;

import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.risk.model.RiskProfileType;

import java.util.List;

public final class ScoringStrategySelector {

    private ScoringStrategySelector() {
    }

    public static RiskProfileType resolveProfileType(Integer bureauScore, List<Loan> customerLoans) {
        boolean hasBureau = bureauScore != null;
        boolean hasHistory = customerLoans != null && customerLoans.stream().anyMatch(ScoringStrategySelector::countsAsHistory);

        if (!hasBureau && !hasHistory) {
            return RiskProfileType.NO_HIT;
        }
        if (!hasHistory) {
            return RiskProfileType.THIN_FILE;
        }
        return RiskProfileType.THICK_FILE;
    }

    private static boolean countsAsHistory(Loan loan) {
        if (loan == null) {
            return false;
        }
        LoanStatus status = loan.getStatus();
        return status == LoanStatus.OPEN
                || status == LoanStatus.PAID
                || status == LoanStatus.OVERDUE
                || status == LoanStatus.CREDITED
                || status == LoanStatus.APPROVED
                || status == LoanStatus.REJECTED
                || status == LoanStatus.CANCELED;
    }
}
