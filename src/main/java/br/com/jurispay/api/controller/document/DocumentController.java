package br.com.jurispay.api.controller.document;

import br.com.jurispay.api.dto.document.DocumentValidationRequest;
import br.com.jurispay.application.document.dto.DocumentResponse;
import br.com.jurispay.application.document.dto.DocumentUploadCommand;
import br.com.jurispay.application.document.dto.DocumentValidationCommand;
import br.com.jurispay.application.document.usecase.GetDocumentByIdUseCase;
import br.com.jurispay.application.document.usecase.ListDocumentsUseCase;
import br.com.jurispay.application.document.usecase.UploadDocumentUseCase;
import br.com.jurispay.application.document.usecase.ValidateDocumentUseCase;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.document.model.Document;
import br.com.jurispay.domain.document.model.DocumentType;
import br.com.jurispay.domain.document.repository.DocumentRepository;
import br.com.jurispay.infrastructure.filestorage.minio.MinioS3FileStorageRepository;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * Controller REST para gerenciamento de documentos.
 */
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final UploadDocumentUseCase uploadDocumentUseCase;
    private final GetDocumentByIdUseCase getDocumentByIdUseCase;
    private final ListDocumentsUseCase listDocumentsUseCase;
    private final ValidateDocumentUseCase validateDocumentUseCase;
    private final DocumentRepository documentRepository;
    private final MinioS3FileStorageRepository fileStorageRepository;

    public DocumentController(
            UploadDocumentUseCase uploadDocumentUseCase,
            GetDocumentByIdUseCase getDocumentByIdUseCase,
            ListDocumentsUseCase listDocumentsUseCase,
            ValidateDocumentUseCase validateDocumentUseCase,
            DocumentRepository documentRepository,
            MinioS3FileStorageRepository fileStorageRepository) {
        this.uploadDocumentUseCase = uploadDocumentUseCase;
        this.getDocumentByIdUseCase = getDocumentByIdUseCase;
        this.listDocumentsUseCase = listDocumentsUseCase;
        this.validateDocumentUseCase = validateDocumentUseCase;
        this.documentRepository = documentRepository;
        this.fileStorageRepository = fileStorageRepository;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> upload(
            @RequestParam("customerId") Long customerId,
            @RequestParam(value = "loanId", required = false) Long loanId,
            @RequestParam("type") DocumentType type,
            @RequestParam("file") MultipartFile file) throws IOException {
        DocumentUploadCommand command = DocumentUploadCommand.builder()
                .customerId(customerId)
                .loanId(loanId)
                .type(type)
                .originalFileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .bytes(file.getBytes())
                .build();

        DocumentResponse response = uploadDocumentUseCase.upload(command);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getById(@PathVariable Long id) {
        DocumentResponse response = getDocumentByIdUseCase.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DocumentResponse>> listAll() {
        List<DocumentResponse> documents = listDocumentsUseCase.listAll();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<List<DocumentResponse>> listByCustomerId(@PathVariable Long customerId) {
        List<DocumentResponse> documents = listDocumentsUseCase.listByCustomerId(customerId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/by-loan/{loanId}")
    public ResponseEntity<List<DocumentResponse>> listByLoanId(@PathVariable Long loanId) {
        List<DocumentResponse> documents = listDocumentsUseCase.listByLoanId(loanId);
        return ResponseEntity.ok(documents);
    }

    @PatchMapping("/{id}/validation")
    public ResponseEntity<DocumentResponse> validate(
            @PathVariable Long id,
            @Valid @RequestBody DocumentValidationRequest request) {
        DocumentValidationCommand command = DocumentValidationCommand.builder()
                .documentId(id)
                .status(request.getStatus())
                .notes(request.getNotes())
                .build();

        DocumentResponse response = validateDocumentUseCase.validate(command);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Documento não encontrado"));

        byte[] bytes = fileStorageRepository.getBytes(document.getBucket(), document.getObjectKey());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(document.getContentType()));
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(document.getOriginalFileName())
                .build());
        headers.setContentLength(bytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }
}

