package br.com.jurispay.application.contract.usecase;

import br.com.jurispay.application.contract.dto.ContractGenerationResponse;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.document.model.Document;
import br.com.jurispay.domain.document.model.DocumentStatus;
import br.com.jurispay.domain.document.model.DocumentType;
import br.com.jurispay.domain.document.model.PutFileCommand;
import br.com.jurispay.domain.document.repository.DocumentRepository;
import br.com.jurispay.domain.document.repository.FileStorageRepository;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import br.com.jurispay.infrastructure.contract.pdf.ContractPdfRenderer;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/**
 * Implementação do caso de uso para gerar PDF do contrato.
 */
@Service
public class GenerateContractPdfUseCaseImpl implements GenerateContractPdfUseCase {

    private static final String DEFAULT_BUCKET = "jurispay-documents";

    private final LoanRepository loanRepository;
    private final DocumentRepository documentRepository;
    private final FileStorageRepository fileStorageRepository;
    private final ContractPdfRenderer contractPdfRenderer;

    public GenerateContractPdfUseCaseImpl(
            LoanRepository loanRepository,
            DocumentRepository documentRepository,
            FileStorageRepository fileStorageRepository,
            ContractPdfRenderer contractPdfRenderer) {
        this.loanRepository = loanRepository;
        this.documentRepository = documentRepository;
        this.fileStorageRepository = fileStorageRepository;
        this.contractPdfRenderer = contractPdfRenderer;
    }

    @Override
    public ContractGenerationResponse generate(Long loanId) {
        // Buscar empréstimo
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Empréstimo não encontrado."));

        // Gerar PDF
        byte[] pdfBytes = contractPdfRenderer.render(loan);

        // Montar nome do arquivo
        String fileName = String.format("contract-loan-%d.pdf", loanId);

        // Gerar objectKey
        String objectKey = generateObjectKey(loan.getCustomerId(), loanId);

        // Salvar no storage
        PutFileCommand putCommand = PutFileCommand.builder()
                .bucket(DEFAULT_BUCKET)
                .objectKey(objectKey)
                .contentType("application/pdf")
                .originalFileName(fileName)
                .bytes(pdfBytes)
                .build();

        fileStorageRepository.put(putCommand);

        // Criar Document no domínio
        Instant now = Instant.now();
        Document document = Document.builder()
                .customerId(loan.getCustomerId())
                .loanId(loan.getId())
                .type(DocumentType.CONTRACT_PDF)
                .status(DocumentStatus.VALIDATED)
                .originalFileName(fileName)
                .contentType("application/pdf")
                .sizeBytes((long) pdfBytes.length)
                .bucket(DEFAULT_BUCKET)
                .objectKey(objectKey)
                .uploadedAt(now)
                .build();

        // Persistir documento
        Document savedDocument = documentRepository.save(document);

        // Retornar resposta
        return ContractGenerationResponse.builder()
                .loanId(loanId)
                .customerId(loan.getCustomerId())
                .documentId(savedDocument.getId())
                .fileName(fileName)
                .generatedAt(now)
                .build();
    }

    private String generateObjectKey(Long customerId, Long loanId) {
        String uuid = UUID.randomUUID().toString();
        return String.format("customers/%d/contracts/loan-%d-%s.pdf", customerId, loanId, uuid);
    }
}

