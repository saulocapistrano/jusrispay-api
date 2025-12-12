package br.com.jurispay.api.dto.document;

import br.com.jurispay.domain.document.model.DocumentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request para validação de documento.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentValidationRequest {

    @NotNull(message = "Status é obrigatório")
    private DocumentStatus status;

    private String notes;
}

