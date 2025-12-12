package br.com.jurispay.application.document.usecase;

import br.com.jurispay.application.document.dto.DocumentResponse;
import br.com.jurispay.application.document.dto.DocumentValidationCommand;
import br.com.jurispay.application.document.mapper.DocumentApplicationMapper;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.document.model.Document;
import br.com.jurispay.domain.document.model.DocumentStatus;
import br.com.jurispay.domain.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Implementação do use case de validação de documentos.
 */
@Service
public class ValidateDocumentUseCaseImpl implements ValidateDocumentUseCase {

    private final DocumentRepository documentRepository;
    private final DocumentApplicationMapper mapper;

    public ValidateDocumentUseCaseImpl(
            DocumentRepository documentRepository,
            DocumentApplicationMapper mapper) {
        this.documentRepository = documentRepository;
        this.mapper = mapper;
    }

    @Override
    public DocumentResponse validate(DocumentValidationCommand command) {
        // Validações
        if (command.getDocumentId() == null) {
            throw new ValidationException("ID do documento é obrigatório.");
        }

        if (command.getStatus() == null) {
            throw new ValidationException("Status de validação é obrigatório.");
        }

        if (command.getStatus() == DocumentStatus.UPLOADED) {
            throw new ValidationException("Status deve ser VALIDATED ou REJECTED.");
        }

        // Buscar documento
        Document document = documentRepository.findById(command.getDocumentId())
                .orElseThrow(() -> new NotFoundException("Documento não encontrado."));

        // Atualizar documento
        Document updatedDocument = Document.builder()
                .id(document.getId())
                .customerId(document.getCustomerId())
                .loanId(document.getLoanId())
                .type(document.getType())
                .status(command.getStatus())
                .originalFileName(document.getOriginalFileName())
                .contentType(document.getContentType())
                .sizeBytes(document.getSizeBytes())
                .bucket(document.getBucket())
                .objectKey(document.getObjectKey())
                .uploadedAt(document.getUploadedAt())
                .validatedAt(Instant.now())
                .notes(command.getNotes())
                .build();

        // Salvar
        Document savedDocument = documentRepository.save(updatedDocument);

        // Retornar response
        return mapper.toResponse(savedDocument);
    }
}

