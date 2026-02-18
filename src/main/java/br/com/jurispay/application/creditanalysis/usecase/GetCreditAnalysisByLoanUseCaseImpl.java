package br.com.jurispay.application.creditanalysis.usecase;

import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;
import br.com.jurispay.application.creditanalysis.mapper.CreditAnalysisApplicationMapper;
import br.com.jurispay.application.creditcheck.usecase.GetLatestCreditCheckByLoanUseCase;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.creditanalysis.repository.CreditAnalysisRepository;
import org.springframework.stereotype.Service;

/**
 * Implementação do use case de busca de análise por empréstimo.
 */
@Service
public class GetCreditAnalysisByLoanUseCaseImpl implements GetCreditAnalysisByLoanUseCase {

    private final CreditAnalysisRepository creditAnalysisRepository;
    private final CreditAnalysisApplicationMapper mapper;
    private final GetLatestCreditCheckByLoanUseCase getLatestCreditCheckByLoanUseCase;

    public GetCreditAnalysisByLoanUseCaseImpl(
            CreditAnalysisRepository creditAnalysisRepository,
            CreditAnalysisApplicationMapper mapper,
            GetLatestCreditCheckByLoanUseCase getLatestCreditCheckByLoanUseCase) {
        this.creditAnalysisRepository = creditAnalysisRepository;
        this.mapper = mapper;
        this.getLatestCreditCheckByLoanUseCase = getLatestCreditCheckByLoanUseCase;
    }

    @Override
    public CreditAnalysisResponse getByLoanId(Long loanId) {
        CreditAnalysisResponse response = creditAnalysisRepository.findByLoanId(loanId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Análise de crédito não encontrada para o empréstimo."));

        return enrichWithCreditCheckSummary(response);
    }

    private CreditAnalysisResponse enrichWithCreditCheckSummary(CreditAnalysisResponse response) {
        if (response == null || response.getLoanId() == null) {
            return response;
        }

        return CreditAnalysisResponse.builder()
                .id(response.getId())
                .loanId(response.getLoanId())
                .customerId(response.getCustomerId())
                .status(response.getStatus())
                .analystUserId(response.getAnalystUserId())
                .startedAt(response.getStartedAt())
                .finishedAt(response.getFinishedAt())
                .decisionDeadlineAt(response.getDecisionDeadlineAt())
                .rejectionReason(response.getRejectionReason())
                .notes(response.getNotes())
                .creditCheckSummary(getLatestCreditCheckByLoanUseCase.getByLoanId(response.getLoanId()).orElse(null))
                .build();
    }
}
