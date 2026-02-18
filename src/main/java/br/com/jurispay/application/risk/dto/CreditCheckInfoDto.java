package br.com.jurispay.application.risk.dto;

import br.com.jurispay.domain.creditcheck.model.CreditCheckDecision;
import br.com.jurispay.domain.creditcheck.model.CreditCheckStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CreditCheckInfoDto {

    private Long id;
    private String providerName;
    private CreditCheckStatus status;
    private CreditCheckDecision decision;
    private Integer bureauScore;
    private Instant finishedAt;
    private Long reusedFromCreditCheckId;
}
