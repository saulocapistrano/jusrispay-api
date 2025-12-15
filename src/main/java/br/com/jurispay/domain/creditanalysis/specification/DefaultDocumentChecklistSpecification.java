package br.com.jurispay.domain.creditanalysis.specification;

import br.com.jurispay.domain.document.model.DocumentType;

import java.util.EnumSet;
import java.util.Set;

/**
 * Implementação padrão da specification de checklist de documentos.
 * Define os 8 tipos de documentos obrigatórios para aprovação de análise de crédito.
 */
public class DefaultDocumentChecklistSpecification implements DocumentChecklistSpecification {

    // Documentos obrigatórios para aprovação (8 itens, excluindo CONTRACT_PDF)
    private static final Set<DocumentType> REQUIRED_DOCUMENT_TYPES = EnumSet.of(
            DocumentType.ADDRESS_PROOF,
            DocumentType.WHATSAPP_LOCATION,
            DocumentType.OCCUPATION_DESCRIPTION,
            DocumentType.SELFIE_WITH_ID,
            DocumentType.WORK_ADDRESS,
            DocumentType.SOCIAL_MEDIA,
            DocumentType.REFERENCE_CONTACTS,
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

