package br.com.jurispay.application.risk.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RiskAssessmentPillarDto {

    private String pillar;
    private Object raw;
    private Integer normalized;
    private Double weight;
    private Integer contribution;
}
