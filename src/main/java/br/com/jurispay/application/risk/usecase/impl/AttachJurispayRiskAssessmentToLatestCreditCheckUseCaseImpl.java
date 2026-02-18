package br.com.jurispay.application.risk.usecase.impl;

import br.com.jurispay.application.risk.usecase.AttachJurispayRiskAssessmentToLatestCreditCheckUseCase;
import br.com.jurispay.domain.creditcheck.model.CreditCheck;
import br.com.jurispay.domain.creditcheck.repository.CreditCheckRepository;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import br.com.jurispay.domain.risk.model.JurispayRiskAssessment;
import br.com.jurispay.domain.risk.scoring.JurispayRiskAssessmentCalculator;
import br.com.jurispay.domain.risk.scoring.ScoringContext;
import br.com.jurispay.infrastructure.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AttachJurispayRiskAssessmentToLatestCreditCheckUseCaseImpl implements AttachJurispayRiskAssessmentToLatestCreditCheckUseCase {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final CreditCheckRepository creditCheckRepository;
    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final JurispayRiskAssessmentCalculator calculator;

    public AttachJurispayRiskAssessmentToLatestCreditCheckUseCaseImpl(
            CreditCheckRepository creditCheckRepository,
            LoanRepository loanRepository,
            CustomerRepository customerRepository,
            JurispayRiskAssessmentCalculator calculator) {
        this.creditCheckRepository = creditCheckRepository;
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.calculator = calculator;
    }

    @Override
    @Transactional
    public void attach(Long loanId) {
        if (loanId == null) {
            return;
        }

        CreditCheck creditCheck = creditCheckRepository.findLatestByLoanId(loanId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BUSINESS_RULE_VIOLATION, "Credit check não encontrado para o empréstimo."));

        var loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.LOAN_NOT_FOUND, "Empréstimo não encontrado."));

        var customer = customerRepository.findById(loan.getCustomerId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND, "Cliente não encontrado."));

        Integer bureauScore = JsonUtil.tryExtractInt(creditCheck.getSummaryJson(), "score");
        var customerLoans = loanRepository.findByCustomerId(customer.getId());

        ScoringContext context = new ScoringContext(
                loanId,
                customer,
                bureauScore,
                customerLoans,
                Instant.now());

        JurispayRiskAssessment assessment = calculator.calculate(context);

        String updatedJson = upsertAssessmentJson(creditCheck.getSummaryJson(), assessment);

        CreditCheck updated = CreditCheck.builder()
                .id(creditCheck.getId())
                .loanId(creditCheck.getLoanId())
                .customerId(creditCheck.getCustomerId())
                .cpf(creditCheck.getCpf())
                .providerName(creditCheck.getProviderName())
                .status(creditCheck.getStatus())
                .decision(creditCheck.getDecision())
                .summaryJson(updatedJson)
                .reusedFromCreditCheckId(creditCheck.getReusedFromCreditCheckId())
                .requestedByUserId(creditCheck.getRequestedByUserId())
                .traceId(creditCheck.getTraceId())
                .createdAt(creditCheck.getCreatedAt())
                .finishedAt(creditCheck.getFinishedAt())
                .build();

        creditCheckRepository.save(updated);
    }

    private String upsertAssessmentJson(String summaryJson, JurispayRiskAssessment assessment) {
        Map<String, Object> map = readJsonAsMap(summaryJson);
        map.put("jurispayRiskAssessment", toMap(assessment));
        try {
            return MAPPER.writeValueAsString(map);
        } catch (Exception e) {
            return summaryJson;
        }
    }

    private Map<String, Object> readJsonAsMap(String json) {
        if (json == null || json.isBlank()) {
            return new LinkedHashMap<>();
        }
        try {
            Map<String, Object> parsed = MAPPER.readValue(json, new TypeReference<>() {
            });
            return parsed != null ? new LinkedHashMap<>(parsed) : new LinkedHashMap<>();
        } catch (Exception ignored) {
            return new LinkedHashMap<>();
        }
    }

    private Map<String, Object> toMap(JurispayRiskAssessment assessment) {
        try {
            return MAPPER.convertValue(assessment, new TypeReference<>() {
            });
        } catch (Exception e) {
            return Map.of(
                    "modelVersion", assessment.modelVersion().name(),
                    "profileType", assessment.profileType().name(),
                    "confidence", assessment.confidence().name(),
                    "finalScore", assessment.finalScore(),
                    "band", assessment.band().name(),
                    "signals", assessment.signals());
        }
    }
}
