package br.com.jurispay.application.report.usecase;

import br.com.jurispay.application.report.dto.OverdueLoanItemResponse;
import br.com.jurispay.application.report.mapper.ReportApplicationMapper;
import br.com.jurispay.domain.report.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do caso de uso para listar empréstimos em atraso.
 */
@Service
public class ListOverdueLoansUseCaseImpl implements ListOverdueLoansUseCase {

    private static final int DEFAULT_LIMIT = 20;
    private static final int MAX_LIMIT = 200;

    private final ReportRepository reportRepository;
    private final ReportApplicationMapper mapper;

    public ListOverdueLoansUseCaseImpl(
            ReportRepository reportRepository,
            ReportApplicationMapper mapper) {
        this.reportRepository = reportRepository;
        this.mapper = mapper;
    }

    @Override
    public List<OverdueLoanItemResponse> list(int limit) {
        int validLimit = validateLimit(limit);
        Instant now = Instant.now();
        return reportRepository.listOverdueLoans(now, validLimit).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    private int validateLimit(int limit) {
        if (limit <= 0 || limit > MAX_LIMIT) {
            return DEFAULT_LIMIT;
        }
        return limit;
    }
}

