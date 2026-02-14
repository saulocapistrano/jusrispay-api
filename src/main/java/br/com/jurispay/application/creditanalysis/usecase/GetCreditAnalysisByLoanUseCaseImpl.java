package br.com.jurispay.application.creditanalysis.usecase;

import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;
import br.com.jurispay.application.creditanalysis.mapper.CreditAnalysisApplicationMapper;
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

    public GetCreditAnalysisByLoanUseCaseImpl(
            CreditAnalysisRepository creditAnalysisRepository,
            CreditAnalysisApplicationMapper mapper) {
        this.creditAnalysisRepository = creditAnalysisRepository;
        this.mapper = mapper;
    }

    @Override
    public CreditAnalysisResponse getByLoanId(Long loanId) {
        return creditAnalysisRepository.findByLoanId(loanId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Análise de crédito não encontrada para o empréstimo."));
    }
}
