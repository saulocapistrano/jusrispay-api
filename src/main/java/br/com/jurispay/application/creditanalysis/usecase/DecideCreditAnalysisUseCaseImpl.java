package br.com.jurispay.application.creditanalysis.usecase;

import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisDecisionCommand;
import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;
import br.com.jurispay.application.creditanalysis.mapper.CreditAnalysisApplicationMapper;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysis;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysisStatus;
import br.com.jurispay.domain.creditanalysis.repository.CreditAnalysisRepository;
import br.com.jurispay.application.creditanalysis.service.CreditAnalysisDecisionValidator;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import br.com.jurispay.domain.document.model.Document;
import br.com.jurispay.domain.document.model.DocumentStatus;
import br.com.jurispay.domain.document.model.DocumentType;
import br.com.jurispay.domain.document.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementação do use case de decisão de análise de crédito.
 */
@Service
public class DecideCreditAnalysisUseCaseImpl implements DecideCreditAnalysisUseCase {

    // Documentos obrigatórios para aprovação (8 itens, excluindo CONTRACT_PDF)
    private static final Set<DocumentType> REQUIRED_DOCUMENT_TYPES = EnumSet.of(
            DocumentType.ADDRESS_PROOF,
            DocumentType.WHATSAPP_LOCATION,
            DocumentType.OCCUPATION_DESCRIPTION,
            DocumentType.SELFIE_WITH_ID,
            DocumentType.WORK_ADDRESS,
            DocumentType.SOCIAL_MEDIA,
            DocumentType.REFERENCE_CONTACTS,
            DocumentType.INCOME_PROOF
    );

    private final CreditAnalysisRepository creditAnalysisRepository;
    private final CustomerRepository customerRepository;
    private final DocumentRepository documentRepository;
    private final CreditAnalysisDecisionValidator decisionValidator;
    private final CreditAnalysisApplicationMapper mapper;

    public DecideCreditAnalysisUseCaseImpl(
            CreditAnalysisRepository creditAnalysisRepository,
            CustomerRepository customerRepository,
            DocumentRepository documentRepository,
            CreditAnalysisDecisionValidator decisionValidator,
            CreditAnalysisApplicationMapper mapper) {
        this.creditAnalysisRepository = creditAnalysisRepository;
        this.customerRepository = customerRepository;
        this.documentRepository = documentRepository;
        this.decisionValidator = decisionValidator;
        this.mapper = mapper;
    }

    @Override
    public CreditAnalysisResponse decide(CreditAnalysisDecisionCommand command) {
        // Validações básicas
        if (command.getCustomerId() == null) {
            throw new ValidationException("ID do cliente é obrigatório.");
        }

        if (command.getAnalystUserId() == null) {
            throw new ValidationException("ID do analista é obrigatório.");
        }

        if (command.getDecisionStatus() == null) {
            throw new ValidationException("Status da decisão é obrigatório.");
        }

        if (command.getDecisionStatus() != CreditAnalysisStatus.APPROVED &&
            command.getDecisionStatus() != CreditAnalysisStatus.REJECTED) {
            throw new ValidationException("Status da decisão deve ser APPROVED ou REJECTED.");
        }

        // Verificar se cliente existe
        customerRepository.findById(command.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));

        // Buscar análise existente
        CreditAnalysis analysis = creditAnalysisRepository.findByCustomerId(command.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Análise de crédito não encontrada para o cliente."));

        // Validar que análise está em andamento
        if (analysis.getStatus() != CreditAnalysisStatus.IN_REVIEW) {
            throw new ValidationException("Análise não está em andamento.");
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

        // Agrupar por tipo e pegar apenas os validados
        Map<DocumentType, List<Document>> documentsByType = documents.stream()
                .filter(doc -> doc.getStatus() == DocumentStatus.VALIDATED)
                .collect(Collectors.groupingBy(Document::getType));

        // Verificar se todos os tipos obrigatórios existem e estão validados
        Set<DocumentType> missingTypes = REQUIRED_DOCUMENT_TYPES.stream()
                .filter(type -> !documentsByType.containsKey(type) || documentsByType.get(type).isEmpty())
                .collect(Collectors.toSet());

        if (!missingTypes.isEmpty()) {
            throw new ValidationException("Checklist de documentos incompleto para aprovação.");
        }
    }
}

