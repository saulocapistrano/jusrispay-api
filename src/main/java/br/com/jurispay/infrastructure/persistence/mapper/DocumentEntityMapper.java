package br.com.jurispay.infrastructure.persistence.mapper;

import br.com.jurispay.domain.document.model.Document;
import br.com.jurispay.infrastructure.persistence.jpa.entity.DocumentEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para conversão entre entidades JPA e modelos de domínio de Document.
 */
@Mapper(componentModel = "spring")
public interface DocumentEntityMapper {

    DocumentEntity toEntity(Document document);

    Document toDomain(DocumentEntity entity);
}

