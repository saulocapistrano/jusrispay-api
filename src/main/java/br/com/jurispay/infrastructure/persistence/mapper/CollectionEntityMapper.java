package br.com.jurispay.infrastructure.persistence.mapper;

import br.com.jurispay.domain.collection.model.CollectionAction;
import br.com.jurispay.infrastructure.persistence.jpa.entity.CollectionActionEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para conversão entre entidades JPA e modelos de domínio de CollectionAction.
 */
@Mapper(componentModel = "spring")
public interface CollectionEntityMapper {

    CollectionActionEntity toEntity(CollectionAction domain);

    CollectionAction toDomain(CollectionActionEntity entity);
}

