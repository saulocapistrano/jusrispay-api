package br.com.jurispay.application.creditcheck.dto;

import br.com.jurispay.domain.creditcheck.model.CreditCheckDecision;
import br.com.jurispay.domain.creditcheck.model.CreditCheckStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CreditCheckSummaryDto {

    private Long id;
    private String providerName;
    private CreditCheckStatus status;
    private CreditCheckDecision decision;
    private Integer score;
    private Instant finishedAt;
    private Long reusedFromCreditCheckId;
}
