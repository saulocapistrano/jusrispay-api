package br.com.jurispay.application.document.dto;

import br.com.jurispay.domain.document.model.DocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Comando para validar/rejeitar um documento.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentValidationCommand {

    private Long documentId;

    /**
     * Status de validação (VALIDATED ou REJECTED).
     */
    private DocumentStatus status;

    /**
     * Observações sobre a validação (opcional).
     */
    private String notes;
}

