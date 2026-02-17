package br.com.jurispay.domain.creditanalysis.specification;

import br.com.jurispay.domain.document.model.DocumentType;

import java.util.EnumSet;
import java.util.Set;

/**
 * Implementação padrão da specification de checklist de documentos.
 * Define os 8 tipos de documentos obrigatórios para aprovação de análise de crédito.
 */
public class DefaultDocumentChecklistSpecification implements DocumentChecklistSpecification {

    // Documentos obrigatórios para aprovação
    private static final Set<DocumentType> REQUIRED_DOCUMENT_TYPES = EnumSet.of(
            DocumentType.ADDRESS_PROOF,
            DocumentType.SELFIE_WITH_ID,
            DocumentType.INCOME_PROOF
    );

    @Override
    public boolean isSatisfiedBy(Set<DocumentType> availableDocuments) {
        if (availableDocuments == null || availableDocuments.isEmpty()) {
            return false;
        }
        // Verifica se todos os tipos obrigatórios estão presentes nos documentos disponíveis
        return availableDocuments.containsAll(REQUIRED_DOCUMENT_TYPES);
    }

    @Override
    public Set<DocumentType> getRequiredDocumentTypes() {
        return EnumSet.copyOf(REQUIRED_DOCUMENT_TYPES);
    }
}

