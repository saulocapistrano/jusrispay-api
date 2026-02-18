package br.com.jurispay.application.report.usecase;

import br.com.jurispay.application.report.dto.PortfolioSummaryResponse;
import br.com.jurispay.application.report.mapper.ReportApplicationMapper;
import br.com.jurispay.domain.report.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Implementação do caso de uso para obter resumo do portfólio.
 */
@Service
public class GetPortfolioSummaryUseCaseImpl implements GetPortfolioSummaryUseCase {

    private final ReportRepository reportRepository;
    private final ReportApplicationMapper mapper;

    public GetPortfolioSummaryUseCaseImpl(
            ReportRepository reportRepository,
            ReportApplicationMapper mapper) {
        this.reportRepository = reportRepository;
        this.mapper = mapper;
    }

    @Override
    public PortfolioSummaryResponse get() {
        Instant now = Instant.now();
        return mapper.toResponse(reportRepository.getPortfolioSummary(now));
    }
}

