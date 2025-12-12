package br.com.jurispay.application.document.usecase;

import br.com.jurispay.application.document.dto.DocumentResponse;
import br.com.jurispay.application.document.mapper.DocumentApplicationMapper;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;

/**
 * Implementação do use case de busca de documento por ID.
 */
@Service
public class GetDocumentByIdUseCaseImpl implements GetDocumentByIdUseCase {

    private final DocumentRepository documentRepository;
    private final DocumentApplicationMapper mapper;

    public GetDocumentByIdUseCaseImpl(
            DocumentRepository documentRepository,
            DocumentApplicationMapper mapper) {
        this.documentRepository = documentRepository;
        this.mapper = mapper;
    }

    @Override
    public DocumentResponse getById(Long id) {
        return documentRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Documento não encontrado."));
    }
}

