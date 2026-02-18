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
        // Given - todos os documentos obrigatórios
        Set<DocumentType> availableDocuments = EnumSet.of(
                DocumentType.ADDRESS_PROOF,
                DocumentType.SELFIE_WITH_ID,
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
                DocumentType.SELFIE_WITH_ID,
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
                DocumentType.SELFIE_WITH_ID,
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
        assertEquals(3, requiredTypes.size());
        assertTrue(requiredTypes.contains(DocumentType.ADDRESS_PROOF));
        assertTrue(requiredTypes.contains(DocumentType.SELFIE_WITH_ID));
        assertTrue(requiredTypes.contains(DocumentType.INCOME_PROOF));
        assertFalse(requiredTypes.contains(DocumentType.CONTRACT_PDF));
    }
}

