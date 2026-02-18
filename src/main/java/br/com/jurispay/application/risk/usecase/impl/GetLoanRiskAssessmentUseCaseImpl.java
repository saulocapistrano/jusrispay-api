package br.com.jurispay.application.risk.usecase.impl;

import br.com.jurispay.application.risk.dto.CreditCheckInfoDto;
import br.com.jurispay.application.risk.dto.JurispayRiskAssessmentDto;
import br.com.jurispay.application.risk.dto.LoanRiskAssessmentResponse;
import br.com.jurispay.application.risk.dto.RiskAssessmentPillarDto;
import br.com.jurispay.application.risk.usecase.GetLoanRiskAssessmentUseCase;
import br.com.jurispay.domain.creditcheck.model.CreditCheck;
import br.com.jurispay.domain.creditcheck.repository.CreditCheckRepository;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import br.com.jurispay.infrastructure.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GetLoanRiskAssessmentUseCaseImpl implements GetLoanRiskAssessmentUseCase {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final LoanRepository loanRepository;
    private final CreditCheckRepository creditCheckRepository;

    public GetLoanRiskAssessmentUseCaseImpl(
            LoanRepository loanRepository,
            CreditCheckRepository creditCheckRepository) {
        this.loanRepository = loanRepository;
        this.creditCheckRepository = creditCheckRepository;
    }

    @Override
    public LoanRiskAssessmentResponse getByLoanId(Long loanId) {
        loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.LOAN_NOT_FOUND, "Empréstimo não encontrado."));

        CreditCheck creditCheck = creditCheckRepository.findLatestByLoanId(loanId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND, "Credit check não encontrado para o empréstimo."));

        Integer bureauScore = JsonUtil.tryExtractInt(creditCheck.getSummaryJson(), "score");

        CreditCheckInfoDto creditCheckInfo = CreditCheckInfoDto.builder()
                .id(creditCheck.getId())
                .providerName(creditCheck.getProviderName())
                .status(creditCheck.getStatus())
                .decision(creditCheck.getDecision())
                .bureauScore(bureauScore)
                .finishedAt(creditCheck.getFinishedAt())
                .reusedFromCreditCheckId(creditCheck.getReusedFromCreditCheckId())
                .build();

        JurispayRiskAssessmentDto assessment = tryParseAssessment(creditCheck.getSummaryJson());

        return LoanRiskAssessmentResponse.builder()
                .loanId(loanId)
                .creditCheck(creditCheckInfo)
                .jurispayRiskAssessment(assessment)
                .build();
    }

    private JurispayRiskAssessmentDto tryParseAssessment(String summaryJson) {
        if (summaryJson == null || summaryJson.isBlank()) {
            return null;
        }

        try {
            Map<String, Object> root = MAPPER.readValue(summaryJson, new TypeReference<>() {
            });
            Object assessmentObj = root.get("jurispayRiskAssessment");
            if (!(assessmentObj instanceof Map<?, ?> assessmentMapAny)) {
                return null;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> assessmentMap = (Map<String, Object>) assessmentMapAny;

            String modelVersion = asString(assessmentMap.get("modelVersion"));
            String profileType = asString(assessmentMap.get("profileType"));
            String confidence = asString(assessmentMap.get("confidence"));
            Integer finalScore = asInteger(assessmentMap.get("finalScore"));
            String band = asString(assessmentMap.get("band"));

            List<String> signals = asStringList(assessmentMap.get("signals"));

            List<RiskAssessmentPillarDto> pillars = new ArrayList<>();
            Object pillarsObj = assessmentMap.get("pillars");
            if (pillarsObj instanceof Map<?, ?> pillarsAny) {
                for (Map.Entry<?, ?> entry : pillarsAny.entrySet()) {
                    if (!(entry.getKey() instanceof String key)) {
                        continue;
                    }
                    if (!(entry.getValue() instanceof Map<?, ?> pillarMapAny)) {
                        continue;
                    }
                    @SuppressWarnings("unchecked")
                    Map<String, Object> pillarMap = (Map<String, Object>) pillarMapAny;

                    pillars.add(RiskAssessmentPillarDto.builder()
                            .pillar(key)
                            .raw(pillarMap.get("raw"))
                            .normalized(asInteger(pillarMap.get("normalized")))
                            .weight(asDouble(pillarMap.get("weight")))
                            .contribution(asInteger(pillarMap.get("contribution")))
                            .build());
                }
            }

            return JurispayRiskAssessmentDto.builder()
                    .modelVersion(modelVersion)
                    .profileType(profileType)
                    .confidence(confidence)
                    .finalScore(finalScore)
                    .band(band)
                    .signals(signals)
                    .pillars(pillars)
                    .build();
        } catch (Exception ignored) {
            return null;
        }
    }

    private String asString(Object obj) {
        return obj != null ? String.valueOf(obj) : null;
    }

    private Integer asInteger(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer i) {
            return i;
        }
        if (obj instanceof Number n) {
            return n.intValue();
        }
        if (obj instanceof String s && !s.isBlank()) {
            try {
                return Integer.valueOf(s);
            } catch (Exception ignored) {
                return null;
            }
        }
        return null;
    }

    private Double asDouble(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Double d) {
            return d;
        }
        if (obj instanceof Number n) {
            return n.doubleValue();
        }
        if (obj instanceof String s && !s.isBlank()) {
            try {
                return Double.valueOf(s);
            } catch (Exception ignored) {
                return null;
            }
        }
        return null;
    }

    private List<String> asStringList(Object obj) {
        if (obj == null) {
            return List.of();
        }
        if (obj instanceof List<?> list) {
            List<String> result = new ArrayList<>(list.size());
            for (Object item : list) {
                if (item != null) {
                    result.add(String.valueOf(item));
                }
            }
            return result;
        }
        return List.of(String.valueOf(obj));
    }
}
