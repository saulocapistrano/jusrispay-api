package br.com.jurispay.application.creditcheck.usecase.impl;

import br.com.jurispay.application.creditcheck.dto.CreditCheckSummaryDto;
import br.com.jurispay.application.creditcheck.usecase.GetLatestCreditCheckByLoanUseCase;
import br.com.jurispay.domain.creditcheck.model.CreditCheck;
import br.com.jurispay.domain.creditcheck.repository.CreditCheckRepository;
import br.com.jurispay.infrastructure.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetLatestCreditCheckByLoanUseCaseImpl implements GetLatestCreditCheckByLoanUseCase {

    private final CreditCheckRepository repository;

    public GetLatestCreditCheckByLoanUseCaseImpl(CreditCheckRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<CreditCheckSummaryDto> getByLoanId(Long loanId) {
        return repository.findLatestByLoanId(loanId)
                .map(this::toSummary);
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
}
