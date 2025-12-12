package br.com.jurispay.application.document.mapper;

import br.com.jurispay.application.document.dto.DocumentUploadCommand;
import br.com.jurispay.application.document.dto.DocumentResponse;
import br.com.jurispay.domain.document.model.Document;
import org.mapstruct.Mapper;

/**
 * Mapper para conversão entre DTOs de aplicação e modelos de domínio de Document.
 */
@Mapper(componentModel = "spring")
public interface DocumentApplicationMapper {

    /**
     * Converte DocumentUploadCommand para Document.
     * Nota: bucket, objectKey, uploadedAt, status serão definidos no Use Case.
     */
    default Document toDomain(DocumentUploadCommand command) {
        if (command == null) {
            return null;
        }

        return Document.builder()
                .customerId(command.getCustomerId())
                .loanId(command.getLoanId())
                .type(command.getType())
                .originalFileName(command.getOriginalFileName())
                .contentType(command.getContentType())
                .sizeBytes((long) (command.getBytes() != null ? command.getBytes().length : 0))
                .build();
    }

    /**
     * Converte Document para DocumentResponse.
     */
    DocumentResponse toResponse(Document document);
}

