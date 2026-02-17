package br.com.jurispay.domain.creditdecisionoverride.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditDecisionOverride {

    private Long id;
    private Long loanId;
    private Long creditCheckId;
    private Long overrideByUserId;
    private String overrideReason;
    private Instant createdAt;
}
