package br.com.jurispay.application.report.mapper;

import br.com.jurispay.application.report.dto.DueLoanItemResponse;
import br.com.jurispay.application.report.dto.OverdueLoanItemResponse;
import br.com.jurispay.application.report.dto.PortfolioSummaryResponse;
import br.com.jurispay.domain.report.model.DueLoanItem;
import br.com.jurispay.domain.report.model.OverdueLoanItem;
import br.com.jurispay.domain.report.model.PortfolioSummary;
import org.mapstruct.Mapper;

/**
 * Mapper para conversão entre modelos de domínio e DTOs de resposta de relatórios.
 */
@Mapper(componentModel = "spring")
public interface ReportApplicationMapper {

    PortfolioSummaryResponse toResponse(PortfolioSummary summary);

    DueLoanItemResponse toResponse(DueLoanItem item);

    OverdueLoanItemResponse toResponse(OverdueLoanItem item);
}

