package br.com.jurispay.application.risk.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoanRiskAssessmentResponse {

    private Long loanId;
    private CreditCheckInfoDto creditCheck;
    private JurispayRiskAssessmentDto jurispayRiskAssessment;
}
