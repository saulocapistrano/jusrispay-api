package br.com.jurispay.application.creditanalysis.usecase;

import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisDecisionCommand;
import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;
import br.com.jurispay.application.creditanalysis.mapper.CreditAnalysisApplicationMapper;
import br.com.jurispay.domain.common.exception.ErrorCode;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysis;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysisStatus;
import br.com.jurispay.domain.creditanalysis.repository.CreditAnalysisRepository;
import br.com.jurispay.domain.creditanalysis.specification.DocumentChecklistSpecification;
import br.com.jurispay.application.creditanalysis.service.CreditAnalysisDecisionValidator;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import br.com.jurispay.domain.document.model.Document;
import br.com.jurispay.domain.document.model.DocumentStatus;
import br.com.jurispay.domain.document.model.DocumentType;
import br.com.jurispay.domain.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementação do use case de decisão de análise de crédito.
 */
@Service
public class DecideCreditAnalysisUseCaseImpl implements DecideCreditAnalysisUseCase {

    private final CreditAnalysisRepository creditAnalysisRepository;
    private final CustomerRepository customerRepository;
    private final DocumentRepository documentRepository;
    private final CreditAnalysisDecisionValidator decisionValidator;
    private final CreditAnalysisApplicationMapper mapper;
    private final DocumentChecklistSpecification documentChecklistSpecification;

    public DecideCreditAnalysisUseCaseImpl(
            CreditAnalysisRepository creditAnalysisRepository,
            CustomerRepository customerRepository,
            DocumentRepository documentRepository,
            CreditAnalysisDecisionValidator decisionValidator,
            CreditAnalysisApplicationMapper mapper,
            DocumentChecklistSpecification documentChecklistSpecification) {
        this.creditAnalysisRepository = creditAnalysisRepository;
        this.customerRepository = customerRepository;
        this.documentRepository = documentRepository;
        this.decisionValidator = decisionValidator;
        this.mapper = mapper;
        this.documentChecklistSpecification = documentChecklistSpecification;
    }

    @Override
    public CreditAnalysisResponse decide(CreditAnalysisDecisionCommand command) {
        // Validações básicas
        if (command.getCustomerId() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "ID do cliente é obrigatório.");
        }

        if (command.getAnalystUserId() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "ID do analista é obrigatório.");
        }

        if (command.getDecisionStatus() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "Status da decisão é obrigatório.");
        }

        if (command.getDecisionStatus() != CreditAnalysisStatus.APPROVED &&
            command.getDecisionStatus() != CreditAnalysisStatus.REJECTED) {
            throw new ValidationException(ErrorCode.INVALID_VALUE, "Status da decisão deve ser APPROVED ou REJECTED.");
        }

        // Verificar se cliente existe
        customerRepository.findById(command.getCustomerId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND, "Cliente não encontrado."));

        // Buscar análise existente
        CreditAnalysis analysis = creditAnalysisRepository.findByCustomerId(command.getCustomerId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CREDIT_ANALYSIS_NOT_FOUND, "Análise de crédito não encontrada para o cliente."));

        // Validar que análise está em andamento
        if (analysis.getStatus() != CreditAnalysisStatus.IN_REVIEW) {
            throw new ValidationException(ErrorCode.ANALYSIS_NOT_IN_REVIEW, "Análise não está em andamento.");
        }

        // Se aprovação, validar checklist de documentos
        if (command.getDecisionStatus() == CreditAnalysisStatus.APPROVED) {
            validateChecklist(command.getCustomerId());
        }

        // Atualizar análise com decisão
        Instant now = Instant.now();
        CreditAnalysis updatedAnalysis = CreditAnalysis.builder()
                .id(analysis.getId())
                .customerId(analysis.getCustomerId())
                .status(command.getDecisionStatus())
                .analystUserId(command.getAnalystUserId())
                .startedAt(analysis.getStartedAt())
                .finishedAt(now)
                .decisionDeadlineAt(analysis.getDecisionDeadlineAt())
                .rejectionReason(command.getDecisionStatus() == CreditAnalysisStatus.REJECTED
                        ? command.getRejectionReason()
                        : null)
                .notes(command.getNotes())
                .createdAt(analysis.getCreatedAt())
                .updatedAt(now)
                .build();

        // Validar regras de decisão
        decisionValidator.validateDecision(updatedAnalysis);

        // Salvar análise
        CreditAnalysis savedAnalysis = creditAnalysisRepository.save(updatedAnalysis);

        // Retornar response
        return mapper.toResponse(savedAnalysis);
    }

    private void validateChecklist(Long customerId) {
        // Buscar todos os documentos do cliente
        List<Document> documents = documentRepository.findByCustomerId(customerId);

        // Extrair tipos de documentos validados
        Set<DocumentType> validatedDocumentTypes = documents.stream()
                .filter(doc -> doc.getStatus() == DocumentStatus.VALIDATED)
                .map(Document::getType)
                .collect(Collectors.toSet());

        // Verificar se o checklist está completo usando specification
        if (!documentChecklistSpecification.isSatisfiedBy(validatedDocumentTypes)) {
            throw new ValidationException(ErrorCode.INCOMPLETE_DOCUMENT_CHECKLIST, "Checklist de documentos incompleto para aprovação.");
        }
    }
}

