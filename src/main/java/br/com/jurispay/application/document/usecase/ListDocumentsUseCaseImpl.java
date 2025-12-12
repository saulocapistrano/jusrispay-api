package br.com.jurispay.application.document.usecase;

import br.com.jurispay.application.document.dto.DocumentResponse;
import br.com.jurispay.application.document.mapper.DocumentApplicationMapper;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do use case de listagem de documentos.
 */
@Service
public class ListDocumentsUseCaseImpl implements ListDocumentsUseCase {

    private final DocumentRepository documentRepository;
    private final DocumentApplicationMapper mapper;

    public ListDocumentsUseCaseImpl(
            DocumentRepository documentRepository,
            DocumentApplicationMapper mapper) {
        this.documentRepository = documentRepository;
        this.mapper = mapper;
    }

    @Override
    public List<DocumentResponse> listAll() {
        return documentRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentResponse> listByCustomerId(Long customerId) {
        if (customerId == null) {
            throw new ValidationException("ID do cliente é obrigatório.");
        }

        return documentRepository.findByCustomerId(customerId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentResponse> listByLoanId(Long loanId) {
        if (loanId == null) {
            throw new ValidationException("ID do empréstimo é obrigatório.");
        }

        return documentRepository.findByLoanId(loanId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}

