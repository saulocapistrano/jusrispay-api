package br.com.jurispay.domain.document.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Modelo de domínio para Document.
 * Representa um documento armazenado no sistema.
 * O arquivo físico fica no MinIO/S3, apenas a referência (bucket + objectKey) fica no banco.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document {

    private Long id;

    /**
     * ID do cliente proprietário do documento.
     */
    private Long customerId;

    /**
     * ID do empréstimo relacionado (opcional).
     * Pode ser null se o documento não estiver vinculado a um empréstimo específico.
     */
    private Long loanId;

    /**
     * Tipo do documento.
     */
    private DocumentType type;

    /**
     * Status de validação do documento.
     */
    private DocumentStatus status;

    /**
     * Nome original do arquivo enviado pelo usuário.
     */
    private String originalFileName;

    /**
     * Content-Type do arquivo (ex: application/pdf, image/jpeg).
     */
    private String contentType;

    /**
     * Tamanho do arquivo em bytes.
     */
    private Long sizeBytes;

    /**
     * Nome do bucket onde o arquivo está armazenado (ex: jurispay-documents).
     */
    private String bucket;

    /**
     * Chave/KEY do objeto no storage (ex: clientes/123/documentos/RG/uuid.pdf).
     * Esta é a referência única do arquivo no MinIO/S3.
     */
    private String objectKey;

    /**
     * Data/hora do upload do arquivo.
     */
    private Instant uploadedAt;

    /**
     * Data/hora da validação do documento (opcional).
     */
    private Instant validatedAt;

    /**
     * Observações internas sobre o documento (não deve conter dados sensíveis).
     */
    private String notes;

    /**
     * Valida o documento com o status e notas fornecidos.
     * Cria uma nova instância com status atualizado e validatedAt atualizado.
     *
     * @param newStatus novo status de validação (VALIDATED ou REJECTED)
     * @param notes observações sobre a validação
     * @return nova instância de Document com status atualizado
     * @throws IllegalArgumentException se newStatus for UPLOADED
     */
    public Document validate(DocumentStatus newStatus, String notes) {
        if (newStatus == DocumentStatus.UPLOADED) {
            throw new IllegalArgumentException("Status de validação deve ser VALIDATED ou REJECTED.");
        }

        return Document.builder()
                .id(this.id)
                .customerId(this.customerId)
                .loanId(this.loanId)
                .type(this.type)
                .status(newStatus)
                .originalFileName(this.originalFileName)
                .contentType(this.contentType)
                .sizeBytes(this.sizeBytes)
                .bucket(this.bucket)
                .objectKey(this.objectKey)
                .uploadedAt(this.uploadedAt)
                .validatedAt(Instant.now())
                .notes(notes)
                .build();
    }
}

