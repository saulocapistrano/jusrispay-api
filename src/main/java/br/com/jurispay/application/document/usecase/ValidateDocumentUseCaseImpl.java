package br.com.jurispay.application.document.usecase;

import br.com.jurispay.application.document.dto.DocumentResponse;
import br.com.jurispay.application.document.dto.DocumentValidationCommand;
import br.com.jurispay.application.document.mapper.DocumentApplicationMapper;
import br.com.jurispay.application.document.validator.DocumentValidationCommandValidator;
import br.com.jurispay.domain.common.exception.ErrorCode;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.document.model.Document;
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
    private final DocumentValidationCommandValidator validator;

    public ValidateDocumentUseCaseImpl(
            DocumentRepository documentRepository,
            DocumentApplicationMapper mapper,
            DocumentValidationCommandValidator validator) {
        this.documentRepository = documentRepository;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public DocumentResponse validate(DocumentValidationCommand command) {
        // Validações
        validator.validate(command);

        // Buscar documento
        Document document = documentRepository.findById(command.getDocumentId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.DOCUMENT_NOT_FOUND, "Documento não encontrado."));

        // Atualizar documento usando método do domínio
        Document updatedDocument = document.validate(command.getStatus(), command.getNotes());

        // Salvar
        Document savedDocument = documentRepository.save(updatedDocument);

        // Retornar response
        return mapper.toResponse(savedDocument);
    }
}

