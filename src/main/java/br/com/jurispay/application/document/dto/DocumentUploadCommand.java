package br.com.jurispay.application.document.dto;

import br.com.jurispay.domain.document.model.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Comando para upload de documento.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUploadCommand {

    private Long customerId;

    /**
     * ID do empréstimo relacionado (opcional).
     */
    private Long loanId;

    private DocumentType type;

    private String originalFileName;

    private String contentType;

    private byte[] bytes;
}

