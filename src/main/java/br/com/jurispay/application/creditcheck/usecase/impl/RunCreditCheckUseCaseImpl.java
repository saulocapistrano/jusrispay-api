package br.com.jurispay.application.creditcheck.usecase.impl;

import br.com.jurispay.application.creditcheck.dto.CreditCheckSummaryDto;
import br.com.jurispay.application.creditcheck.usecase.RunCreditCheckUseCase;
import br.com.jurispay.domain.creditcheck.model.CreditCheck;
import br.com.jurispay.domain.creditcheck.model.CreditCheckDecision;
import br.com.jurispay.domain.creditcheck.model.CreditCheckStatus;
import br.com.jurispay.domain.creditcheck.provider.CreditDataProvider;
import br.com.jurispay.domain.creditcheck.provider.dto.CreditDataProviderResult;
import br.com.jurispay.domain.creditcheck.repository.CreditCheckRepository;
import br.com.jurispay.infrastructure.creditcheck.CreditDataProviderResiliencePolicy;
import br.com.jurispay.infrastructure.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class RunCreditCheckUseCaseImpl implements RunCreditCheckUseCase {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final CreditCheckRepository repository;
    private final CreditDataProvider provider;
    private final CreditDataProviderResiliencePolicy resiliencePolicy;
    private final Duration cacheTtl;

    public RunCreditCheckUseCaseImpl(
            CreditCheckRepository repository,
            CreditDataProvider provider,
            CreditDataProviderResiliencePolicy resiliencePolicy,
            @Value("${app.credit-check.cache-ttl-hours:24}") long cacheTtlHours) {
        this.repository = repository;
        this.provider = provider;
        this.resiliencePolicy = resiliencePolicy;
        this.cacheTtl = Duration.ofHours(cacheTtlHours);
    }

    @Override
    public CreditCheckSummaryDto run(Long loanId, Long customerId, String cpf, Long requestedByUserId) {
        String traceId = String.valueOf(System.currentTimeMillis());
        Instant now = Instant.now();

        Instant since = now.minus(cacheTtl);
        var cached = repository.findLatestCompletedByCpfSince(cpf, since);
        if (cached.isPresent()) {
            CreditCheck reused = buildReused(loanId, customerId, cpf, requestedByUserId, cached.get(), traceId, now);
            CreditCheck saved = repository.save(reused);
            return toSummary(saved);
        }

        CreditCheck pending = CreditCheck.builder()
                .loanId(loanId)
                .customerId(customerId)
                .cpf(cpf)
                .providerName("FAKE")
                .status(CreditCheckStatus.PENDING)
                .decision(CreditCheckDecision.MANUAL_REVIEW)
                .summaryJson(null)
                .requestedByUserId(requestedByUserId)
                .traceId(traceId)
                .createdAt(now)
                .finishedAt(null)
                .build();

        CreditCheck created = repository.save(pending);

        try {
            CreditDataProviderResult result = resiliencePolicy.execute(() -> provider.consultByCpf(cpf, traceId));
            CreditCheckDecision decision = decide(result);

            String summaryJson = toSummaryJson(result, decision);

            CreditCheck completed = CreditCheck.builder()
                    .id(created.getId())
                    .loanId(created.getLoanId())
                    .customerId(created.getCustomerId())
                    .cpf(created.getCpf())
                    .providerName(created.getProviderName())
                    .status(CreditCheckStatus.COMPLETED)
                    .decision(decision)
                    .summaryJson(summaryJson)
                    .reusedFromCreditCheckId(null)
                    .requestedByUserId(created.getRequestedByUserId())
                    .traceId(created.getTraceId())
                    .createdAt(created.getCreatedAt())
                    .finishedAt(Instant.now())
                    .build();

            CreditCheck saved = repository.save(completed);
            return toSummary(saved);
        } catch (Exception ex) {
            Map<String, Object> errorSummary = new HashMap<>();
            errorSummary.put("error", "PROVIDER_FAILURE");

            CreditCheck failed = CreditCheck.builder()
                    .id(created.getId())
                    .loanId(created.getLoanId())
                    .customerId(created.getCustomerId())
                    .cpf(created.getCpf())
                    .providerName(created.getProviderName())
                    .status(CreditCheckStatus.FAILED)
                    .decision(CreditCheckDecision.MANUAL_REVIEW)
                    .summaryJson(writeJson(errorSummary))
                    .reusedFromCreditCheckId(null)
                    .requestedByUserId(created.getRequestedByUserId())
                    .traceId(created.getTraceId())
                    .createdAt(created.getCreatedAt())
                    .finishedAt(Instant.now())
                    .build();

            CreditCheck saved = repository.save(failed);
            return toSummary(saved);
        }
    }

    private CreditCheckDecision decide(CreditDataProviderResult result) {
        if (result == null || result.getScore() == null) {
            return CreditCheckDecision.MANUAL_REVIEW;
        }
        if (result.getScore() < 500) {
            return CreditCheckDecision.REJECTED;
        }
        return CreditCheckDecision.APPROVED;
    }

    private CreditCheck buildReused(
            Long loanId,
            Long customerId,
            String cpf,
            Long requestedByUserId,
            CreditCheck cached,
            String traceId,
            Instant now) {
        return CreditCheck.builder()
                .loanId(loanId)
                .customerId(customerId)
                .cpf(cpf)
                .providerName(cached.getProviderName())
                .status(CreditCheckStatus.COMPLETED)
                .decision(cached.getDecision())
                .summaryJson(cached.getSummaryJson())
                .reusedFromCreditCheckId(cached.getId())
                .requestedByUserId(requestedByUserId)
                .traceId(traceId)
                .createdAt(now)
                .finishedAt(now)
                .build();
    }

    private CreditCheckSummaryDto toSummary(CreditCheck creditCheck) {
        Integer score = JsonUtil.tryExtractInt(creditCheck.getSummaryJson(), "score");
        return CreditCheckSummaryDto.builder()
                .id(creditCheck.getId())
                .providerName(creditCheck.getProviderName())
                .status(creditCheck.getStatus())
                .decision(creditCheck.getDecision())
                .score(score)
                .finishedAt(creditCheck.getFinishedAt())
                .reusedFromCreditCheckId(creditCheck.getReusedFromCreditCheckId())
                .build();
    }

    private String toSummaryJson(CreditDataProviderResult result, CreditCheckDecision decision) {
        Map<String, Object> summary = new HashMap<>();
        summary.put("score", result.getScore());
        summary.put("decision", decision.name());
        summary.put("phones", result.getPhones());
        summary.put("emails", result.getEmails());
        summary.put("addresses", result.getAddresses());
        summary.put("flags", result.getFlags());
        return writeJson(summary);
    }

    private String writeJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            return null;
        }
    }
}
