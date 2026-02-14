package br.com.jurispay.application.customer.dto;

import br.com.jurispay.domain.document.model.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerKycStatusResponse {

    private Long customerId;
    private boolean complete;
    private boolean needsRevalidation;
    private Instant lastValidatedAt;
    private Instant lastMovementAt;
    private List<DocumentType> missingDocumentTypes;
}
