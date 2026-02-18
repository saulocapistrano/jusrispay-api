package br.com.jurispay.domain.creditcheck.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCheck {

    private Long id;
    private Long loanId;
    private Long customerId;
    private String cpf;
    private String providerName;
    private CreditCheckStatus status;
    private CreditCheckDecision decision;
    private String summaryJson;
    private Long reusedFromCreditCheckId;
    private Long requestedByUserId;
    private String traceId;
    private Instant createdAt;
    private Instant finishedAt;
}
