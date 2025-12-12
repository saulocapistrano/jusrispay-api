package br.com.jurispay.application.document.usecase;

import br.com.jurispay.application.document.dto.DocumentResponse;
import br.com.jurispay.application.document.dto.DocumentValidationCommand;
import br.com.jurispay.application.document.mapper.DocumentApplicationMapper;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.document.model.Document;
import br.com.jurispay.domain.document.model.DocumentStatus;
import br.com.jurispay.domain.document.model.DocumentType;
import br.com.jurispay.domain.document.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateDocumentUseCaseImplTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentApplicationMapper mapper;

    @InjectMocks
    private ValidateDocumentUseCaseImpl useCase;

    private Document document;
    private DocumentValidationCommand command;
    private DocumentResponse response;

    @BeforeEach
    void setUp() {
        document = Document.builder()
                .id(1L)
                .customerId(1L)
                .type(DocumentType.ADDRESS_PROOF)
                .status(DocumentStatus.UPLOADED)
                .originalFileName("comprovante.pdf")
                .contentType("application/pdf")
                .sizeBytes(1024L)
                .bucket("jurispay-documents")
                .objectKey("customers/1/documents/address_proof/uuid-comprovante.pdf")
                .uploadedAt(Instant.now())
                .build();

        command = DocumentValidationCommand.builder()
                .documentId(1L)
                .status(DocumentStatus.VALIDATED)
                .notes("Documento aprovado")
                .build();

        response = DocumentResponse.builder()
                .id(1L)
                .customerId(1L)
                .type(DocumentType.ADDRESS_PROOF)
                .status(DocumentStatus.VALIDATED)
                .originalFileName("comprovante.pdf")
                .contentType("application/pdf")
                .sizeBytes(1024L)
                .bucket("jurispay-documents")
                .objectKey("customers/1/documents/address_proof/uuid-comprovante.pdf")
                .uploadedAt(Instant.now())
                .validatedAt(Instant.now())
                .notes("Documento aprovado")
                .build();
    }

    @Test
    void shouldValidateDocumentWhenValidData() {
        // Given
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(documentRepository.save(any(Document.class))).thenReturn(document);
        when(mapper.toResponse(any(Document.class))).thenReturn(response);

        // When
        DocumentResponse result = useCase.validate(command);

        // Then
        assertNotNull(result);
        assertEquals(DocumentStatus.VALIDATED, result.getStatus());
        assertNotNull(result.getValidatedAt());
        verify(documentRepository).findById(1L);
        verify(documentRepository).save(any(Document.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDocumentDoesNotExist() {
        // Given
        when(documentRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            useCase.validate(command);
        });

        assertEquals("Documento não encontrado.", exception.getMessage());
        verify(documentRepository).findById(1L);
        verify(documentRepository, never()).save(any());
    }
}

