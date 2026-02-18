package br.com.jurispay.application.document.usecase;

import br.com.jurispay.application.document.dto.DocumentResponse;
import br.com.jurispay.application.document.dto.DocumentUploadCommand;
import br.com.jurispay.application.document.mapper.DocumentApplicationMapper;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import br.com.jurispay.domain.document.model.Document;
import br.com.jurispay.domain.document.model.DocumentStatus;
import br.com.jurispay.domain.document.model.PutFileCommand;
import br.com.jurispay.domain.document.repository.DocumentRepository;
import br.com.jurispay.domain.document.repository.FileStorageRepository;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/**
 * Implementação do use case de upload de documentos.
 */
@Service
public class UploadDocumentUseCaseImpl implements UploadDocumentUseCase {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String DEFAULT_BUCKET = "jurispay-documents";

    private final DocumentRepository documentRepository;
    private final FileStorageRepository fileStorageRepository;
    private final CustomerRepository customerRepository;
    private final LoanRepository loanRepository;
    private final DocumentApplicationMapper mapper;

    public UploadDocumentUseCaseImpl(
            DocumentRepository documentRepository,
            FileStorageRepository fileStorageRepository,
            CustomerRepository customerRepository,
            LoanRepository loanRepository,
            DocumentApplicationMapper mapper) {
        this.documentRepository = documentRepository;
        this.fileStorageRepository = fileStorageRepository;
        this.customerRepository = customerRepository;
        this.loanRepository = loanRepository;
        this.mapper = mapper;
    }

    @Override
    public DocumentResponse upload(DocumentUploadCommand command) {
        // Validações básicas
        validateCommand(command);

        // Validar cliente existe
        customerRepository.findById(command.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado para upload de documento."));

        // Validar empréstimo se fornecido
        if (command.getLoanId() != null) {
            loanRepository.findById(command.getLoanId())
                    .orElseThrow(() -> new NotFoundException("Empréstimo não encontrado para vincular documento."));
        }

        // Gerar objectKey
        String objectKey = generateObjectKey(command.getCustomerId(), command.getType(), command.getOriginalFileName());

        // Enviar para storage
        PutFileCommand putCommand = PutFileCommand.builder()
                .bucket(DEFAULT_BUCKET)
                .objectKey(objectKey)
                .contentType(command.getContentType())
                .originalFileName(command.getOriginalFileName())
                .bytes(command.getBytes())
                .build();

        fileStorageRepository.put(putCommand);

        // Criar Document no domínio
        Document document = mapper.toDomain(command);
        document = Document.builder()
                .customerId(document.getCustomerId())
                .loanId(document.getLoanId())
                .type(document.getType())
                .status(DocumentStatus.UPLOADED)
                .originalFileName(document.getOriginalFileName())
                .contentType(document.getContentType())
                .sizeBytes(document.getSizeBytes())
                .bucket(DEFAULT_BUCKET)
                .objectKey(objectKey)
                .uploadedAt(Instant.now())
                .build();

        // Salvar documento
        Document savedDocument = documentRepository.save(document);

        // Retornar response
        return mapper.toResponse(savedDocument);
    }

    private void validateCommand(DocumentUploadCommand command) {
        if (command.getCustomerId() == null) {
            throw new ValidationException("ID do cliente é obrigatório.");
        }

        if (command.getType() == null) {
            throw new ValidationException("Tipo do documento é obrigatório.");
        }

        if (command.getBytes() == null || command.getBytes().length == 0) {
            throw new ValidationException("Arquivo não pode estar vazio.");
        }

        // Validar tipo de arquivo permitido
        String contentType = command.getContentType();
        if (contentType == null || 
            (!contentType.equals("application/pdf") && 
             !contentType.equals("image/jpeg") && 
             !contentType.equals("image/png"))) {
            throw new ValidationException("Tipo de arquivo não permitido. Use PDF, JPG ou PNG.");
        }

        // Validar tamanho máximo
        if (command.getBytes().length > MAX_FILE_SIZE) {
            throw new ValidationException("Arquivo excede o tamanho máximo permitido (10MB).");
        }
    }

    private String generateObjectKey(Long customerId, br.com.jurispay.domain.document.model.DocumentType type, String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String sanitizedFileName = sanitizeFileName(originalFileName);
        return String.format("customers/%d/documents/%s/%s-%s", 
                customerId, 
                type.name().toLowerCase(), 
                uuid, 
                sanitizedFileName);
    }

    private String sanitizeFileName(String fileName) {
        if (fileName == null) {
            return "file";
        }
        // Remove caracteres especiais e espaços, mantém apenas alfanuméricos, pontos e hífens
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}

