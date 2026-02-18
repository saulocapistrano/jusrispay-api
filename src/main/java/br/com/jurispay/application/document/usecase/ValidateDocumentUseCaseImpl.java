package br.com.jurispay.application.document.usecase;

import br.com.jurispay.application.document.dto.DocumentResponse;
import br.com.jurispay.application.document.dto.DocumentValidationCommand;
import br.com.jurispay.application.document.mapper.DocumentApplicationMapper;
import br.com.jurispay.application.document.validator.DocumentValidationCommandValidator;
import br.com.jurispay.application.customer.service.CustomerKycService;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.document.model.Document;
import br.com.jurispay.domain.document.repository.DocumentRepository;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Implementação do use case de validação de documentos.
 */
@Service
public class ValidateDocumentUseCaseImpl implements ValidateDocumentUseCase {

    private final DocumentRepository documentRepository;
    private final DocumentApplicationMapper mapper;
    private final DocumentValidationCommandValidator validator;
    private final CustomerKycService customerKycService;
    private final LoanRepository loanRepository;

    public ValidateDocumentUseCaseImpl(
            DocumentRepository documentRepository,
            DocumentApplicationMapper mapper,
            DocumentValidationCommandValidator validator,
            CustomerKycService customerKycService,
            LoanRepository loanRepository) {
        this.documentRepository = documentRepository;
        this.mapper = mapper;
        this.validator = validator;
        this.customerKycService = customerKycService;
        this.loanRepository = loanRepository;
    }

    @Override
    @Transactional
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

        try {
            Instant now = Instant.now();
            var kycStatus = customerKycService.getStatus(savedDocument.getCustomerId(), now);
            if (kycStatus != null && kycStatus.isComplete()) {
                List<Loan> customerLoans = loanRepository.findByCustomerId(savedDocument.getCustomerId());
                for (Loan loan : customerLoans) {
                    if (loan != null && loan.getStatus() == LoanStatus.PENDING_DOCUMENTS) {
                        loanRepository.save(loan.markAsRequested(now));
                    }
                }
            }
        } catch (Exception ignored) {
        }

        // Retornar response
        return mapper.toResponse(savedDocument);
    }
}

