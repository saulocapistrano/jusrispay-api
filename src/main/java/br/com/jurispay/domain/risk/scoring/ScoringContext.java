package br.com.jurispay.domain.risk.scoring;

import br.com.jurispay.domain.customer.model.Customer;
import br.com.jurispay.domain.loan.model.Loan;

import java.time.Instant;
import java.util.List;

public record ScoringContext(
        Long loanId,
        Customer customer,
        Integer bureauScore,
        List<Loan> customerLoans,
        Instant now
) {
}
