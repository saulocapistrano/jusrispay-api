package br.com.jurispay.application.creditanalysis.usecase;

import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;
import br.com.jurispay.application.creditanalysis.mapper.CreditAnalysisApplicationMapper;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.creditanalysis.repository.CreditAnalysisRepository;
import org.springframework.stereotype.Service;

/**
 * Implementação do use case de busca de análise por cliente.
 */
@Service
public class GetCreditAnalysisByCustomerUseCaseImpl implements GetCreditAnalysisByCustomerUseCase {

    private final CreditAnalysisRepository creditAnalysisRepository;
    private final CreditAnalysisApplicationMapper mapper;

    public GetCreditAnalysisByCustomerUseCaseImpl(
            CreditAnalysisRepository creditAnalysisRepository,
            CreditAnalysisApplicationMapper mapper) {
        this.creditAnalysisRepository = creditAnalysisRepository;
        this.mapper = mapper;
    }

    @Override
    public CreditAnalysisResponse getByCustomerId(Long customerId) {
        return creditAnalysisRepository.findByCustomerId(customerId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Análise de crédito não encontrada para o cliente."));
    }
}

