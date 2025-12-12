package br.com.jurispay.application.document.dto;

import br.com.jurispay.domain.document.model.DocumentStatus;
import br.com.jurispay.domain.document.model.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Resposta padrão de documento.
 * Não inclui o conteúdo binário do arquivo (bytes).
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {

    private Long id;

    private Long customerId;

    private Long loanId;

    private DocumentType type;

    private DocumentStatus status;

    private String bucket;

    private String objectKey;

    private String originalFileName;

    private String contentType;

    private Long sizeBytes;

    private Instant uploadedAt;

    private Instant validatedAt;

    private String notes;
}

