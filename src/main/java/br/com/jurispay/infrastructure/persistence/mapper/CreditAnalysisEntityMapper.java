package br.com.jurispay.infrastructure.persistence.mapper;

import br.com.jurispay.domain.creditanalysis.model.CreditAnalysis;
import br.com.jurispay.infrastructure.persistence.jpa.entity.CreditAnalysisEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para conversão entre entidades JPA e modelos de domínio de CreditAnalysis.
 */
@Mapper(componentModel = "spring")
public interface CreditAnalysisEntityMapper {

    CreditAnalysisEntity toEntity(CreditAnalysis domain);

    CreditAnalysis toDomain(CreditAnalysisEntity entity);
}

