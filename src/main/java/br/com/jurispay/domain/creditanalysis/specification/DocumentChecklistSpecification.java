package br.com.jurispay.domain.creditanalysis.specification;

import br.com.jurispay.domain.document.model.DocumentType;

import java.util.Set;

/**
 * Specification Pattern para validação de checklist de documentos.
 * Define a regra de negócio que determina se um conjunto de documentos atende aos requisitos.
 */
public interface DocumentChecklistSpecification {

    /**
     * Verifica se o conjunto de documentos disponíveis satisfaz os requisitos da specification.
     *
     * @param availableDocuments conjunto de tipos de documentos disponíveis e validados
     * @return true se todos os documentos obrigatórios estão presentes, false caso contrário
     */
    boolean isSatisfiedBy(Set<DocumentType> availableDocuments);

    /**
     * Retorna o conjunto de tipos de documentos obrigatórios para aprovação.
     *
     * @return conjunto de tipos de documentos obrigatórios
     */
    Set<DocumentType> getRequiredDocumentTypes();
}

