package br.com.jurispay.application.risk.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class JurispayRiskAssessmentDto {

    private String modelVersion;
    private String profileType;
    private String confidence;
    private Integer finalScore;
    private String band;
    private List<String> signals;
    private List<RiskAssessmentPillarDto> pillars;
}
