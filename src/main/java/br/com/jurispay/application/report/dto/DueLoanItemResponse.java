package br.com.jurispay.application.report.dto;

import br.com.jurispay.domain.loan.model.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Resposta com item de empréstimo com vencimento próximo.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DueLoanItemResponse {

    private Long loanId;

    private Long customerId;

    private Instant dueDate;

    private BigDecimal expectedAmount;

    private LoanStatus status;
}

