package br.com.jurispay.application.document.usecase;

import br.com.jurispay.application.document.dto.DocumentResponse;
import br.com.jurispay.application.document.dto.DocumentUploadCommand;
import br.com.jurispay.application.document.mapper.DocumentApplicationMapper;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.customer.model.Customer;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import br.com.jurispay.domain.document.model.Document;
import br.com.jurispay.domain.document.model.DocumentStatus;
import br.com.jurispay.domain.document.model.DocumentType;
import br.com.jurispay.domain.document.model.StoredFile;
import br.com.jurispay.domain.document.repository.DocumentRepository;
import br.com.jurispay.domain.document.repository.FileStorageRepository;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadDocumentUseCaseImplTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private FileStorageRepository fileStorageRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private DocumentApplicationMapper mapper;

    @InjectMocks
    private UploadDocumentUseCaseImpl useCase;

    private DocumentUploadCommand validCommand;
    private Customer customer;
    private Document document;
    private DocumentResponse response;

    @BeforeEach
    void setUp() {
        byte[] fileBytes = new byte[1024]; // 1KB

        validCommand = DocumentUploadCommand.builder()
                .customerId(1L)
                .type(DocumentType.ADDRESS_PROOF)
                .originalFileName("comprovante.pdf")
                .contentType("application/pdf")
                .bytes(fileBytes)
                .build();

        customer = Customer.builder()
                .id(1L)
                .nomeCompleto("João Silva")
                .cpf("12345678901")
                .telefone("11999999999")
                .rendaMensal(new BigDecimal("5000.00"))
                .dataCriacao(Instant.now())
                .dataAtualizacao(Instant.now())
                .build();

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

        response = DocumentResponse.builder()
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
    }

    @Test
    void shouldUploadDocumentWhenValidData() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(fileStorageRepository.put(any())).thenReturn(StoredFile.builder()
                .bucket("jurispay-documents")
                .objectKey("customers/1/documents/address_proof/uuid-comprovante.pdf")
                .contentType("application/pdf")
                .originalFileName("comprovante.pdf")
                .sizeBytes(1024L)
                .build());
        when(mapper.toDomain(validCommand)).thenReturn(document);
        when(documentRepository.save(any(Document.class))).thenReturn(document);
        when(mapper.toResponse(document)).thenReturn(response);

        // When
        DocumentResponse result = useCase.upload(validCommand);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(customerRepository).findById(1L);
        verify(fileStorageRepository).put(any());
        verify(documentRepository).save(any(Document.class));
    }

    @Test
    void shouldThrowValidationExceptionWhenContentTypeIsInvalid() {
        // Given
        DocumentUploadCommand invalidCommand = DocumentUploadCommand.builder()
                .customerId(1L)
                .type(DocumentType.ADDRESS_PROOF)
                .originalFileName("arquivo.txt")
                .contentType("text/plain")
                .bytes(new byte[1024])
                .build();

        // When/Then
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            useCase.upload(invalidCommand);
        });

        assertEquals("Tipo de arquivo não permitido. Use PDF, JPG ou PNG.", exception.getMessage());
        verify(customerRepository, never()).findById(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenCustomerDoesNotExist() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            useCase.upload(validCommand);
        });

        assertEquals("Cliente não encontrado para upload de documento.", exception.getMessage());
        verify(customerRepository).findById(1L);
        verify(fileStorageRepository, never()).put(any());
    }
}

