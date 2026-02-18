package br.com.jurispay.infrastructure.persistence.mapper;

import br.com.jurispay.domain.creditdecisionoverride.model.CreditDecisionOverride;
import br.com.jurispay.infrastructure.persistence.jpa.entity.CreditDecisionOverrideEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditDecisionOverrideEntityMapper {

    CreditDecisionOverrideEntity toEntity(CreditDecisionOverride domain);

    CreditDecisionOverride toDomain(CreditDecisionOverrideEntity entity);
}
