package br.com.jurispay.domain.creditanalysis.specification;

import br.com.jurispay.domain.document.model.DocumentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para DefaultDocumentChecklistSpecification.
 */
class DefaultDocumentChecklistSpecificationTest {

    private DefaultDocumentChecklistSpecification specification;

    @BeforeEach
    void setUp() {
        specification = new DefaultDocumentChecklistSpecification();
    }

    @Test
    void shouldReturnTrueWhenAllRequiredDocumentsArePresent() {
        // Given - todos os 8 documentos obrigatórios
        Set<DocumentType> availableDocuments = EnumSet.of(
                DocumentType.ADDRESS_PROOF,
                DocumentType.WHATSAPP_LOCATION,
                DocumentType.OCCUPATION_DESCRIPTION,
                DocumentType.SELFIE_WITH_ID,
                DocumentType.WORK_ADDRESS,
                DocumentType.SOCIAL_MEDIA,
                DocumentType.REFERENCE_CONTACTS,
                DocumentType.INCOME_PROOF
        );

        // When
        boolean satisfied = specification.isSatisfiedBy(availableDocuments);

        // Then
        assertTrue(satisfied);
    }

    @Test
    void shouldReturnTrueWhenAllRequiredDocumentsPlusContractPdfArePresent() {
        // Given - todos os obrigatórios + CONTRACT_PDF (que não é obrigatório)
        Set<DocumentType> availableDocuments = EnumSet.of(
                DocumentType.ADDRESS_PROOF,
                DocumentType.WHATSAPP_LOCATION,
                DocumentType.OCCUPATION_DESCRIPTION,
                DocumentType.SELFIE_WITH_ID,
                DocumentType.WORK_ADDRESS,
                DocumentType.SOCIAL_MEDIA,
                DocumentType.REFERENCE_CONTACTS,
                DocumentType.INCOME_PROOF,
                DocumentType.CONTRACT_PDF
        );

        // When
        boolean satisfied = specification.isSatisfiedBy(availableDocuments);

        // Then
        assertTrue(satisfied);
    }

    @Test
    void shouldReturnFalseWhenOneRequiredDocumentIsMissing() {
        // Given - faltando ADDRESS_PROOF
        Set<DocumentType> availableDocuments = EnumSet.of(
                DocumentType.WHATSAPP_LOCATION,
                DocumentType.OCCUPATION_DESCRIPTION,
                DocumentType.SELFIE_WITH_ID,
                DocumentType.WORK_ADDRESS,
                DocumentType.SOCIAL_MEDIA,
                DocumentType.REFERENCE_CONTACTS,
                DocumentType.INCOME_PROOF
        );

        // When
        boolean satisfied = specification.isSatisfiedBy(availableDocuments);

        // Then
        assertFalse(satisfied);
    }

    @Test
    void shouldReturnFalseWhenSetIsEmpty() {
        // Given
        Set<DocumentType> availableDocuments = EnumSet.noneOf(DocumentType.class);

        // When
        boolean satisfied = specification.isSatisfiedBy(availableDocuments);

        // Then
        assertFalse(satisfied);
    }

    @Test
    void shouldReturnFalseWhenSetIsNull() {
        // When
        boolean satisfied = specification.isSatisfiedBy(null);

        // Then
        assertFalse(satisfied);
    }

    @Test
    void shouldReturnRequiredDocumentTypes() {
        // When
        Set<DocumentType> requiredTypes = specification.getRequiredDocumentTypes();

        // Then
        assertNotNull(requiredTypes);
        assertEquals(8, requiredTypes.size());
        assertTrue(requiredTypes.contains(DocumentType.ADDRESS_PROOF));
        assertTrue(requiredTypes.contains(DocumentType.WHATSAPP_LOCATION));
        assertTrue(requiredTypes.contains(DocumentType.OCCUPATION_DESCRIPTION));
        assertTrue(requiredTypes.contains(DocumentType.SELFIE_WITH_ID));
        assertTrue(requiredTypes.contains(DocumentType.WORK_ADDRESS));
        assertTrue(requiredTypes.contains(DocumentType.SOCIAL_MEDIA));
        assertTrue(requiredTypes.contains(DocumentType.REFERENCE_CONTACTS));
        assertTrue(requiredTypes.contains(DocumentType.INCOME_PROOF));
        assertFalse(requiredTypes.contains(DocumentType.CONTRACT_PDF));
    }
}

