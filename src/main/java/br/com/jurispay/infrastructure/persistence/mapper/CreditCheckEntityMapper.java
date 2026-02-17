package br.com.jurispay.infrastructure.persistence.mapper;

import br.com.jurispay.domain.creditcheck.model.CreditCheck;
import br.com.jurispay.infrastructure.persistence.jpa.entity.CreditCheckEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditCheckEntityMapper {

    CreditCheckEntity toEntity(CreditCheck domain);

    CreditCheck toDomain(CreditCheckEntity entity);
}
