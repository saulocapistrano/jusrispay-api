package br.com.jurispay.application.creditanalysis.mapper;

import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversão entre DTOs de aplicação e modelos de domínio de CreditAnalysis.
 */
@Mapper(componentModel = "spring")
public interface CreditAnalysisApplicationMapper {

    /**
     * Converte CreditAnalysis para CreditAnalysisResponse.
     */
    @Mapping(target = "creditCheckSummary", ignore = true)
    CreditAnalysisResponse toResponse(CreditAnalysis analysis);
}

