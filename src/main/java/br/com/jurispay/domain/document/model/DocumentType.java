package br.com.jurispay.domain.document.model;

/**
 * Tipos de documentos aceitos no sistema.
 */
public enum DocumentType {
    ADDRESS_PROOF,          // comprovante de endereço
    WHATSAPP_LOCATION,      // localização whatsapp (pode ser print/pdf)
    OCCUPATION_DESCRIPTION,  // descrição ocupação
    SELFIE_WITH_ID,         // foto com documento
    WORK_ADDRESS,           // endereço/local de trabalho
    SOCIAL_MEDIA,           // redes sociais
    REFERENCE_CONTACTS,     // contatos de referência (pode ser arquivo)
    INCOME_PROOF,           // contracheque/rendimento
    CONTRACT_PDF            // contrato gerado (opcional)
}

