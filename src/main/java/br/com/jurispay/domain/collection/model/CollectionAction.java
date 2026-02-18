package br.com.jurispay.domain.collection.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Representa um registro de cobrança (contato) no domínio.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionAction {

    private Long id;

    /**
     * ID do empréstimo relacionado à cobrança.
     */
    private Long loanId;

    /**
     * Data/hora em que a ação de cobrança foi realizada.
     */
    private Instant actionAt;

    /**
     * Canal utilizado para a cobrança.
     */
    private CollectionChannel channel;

    /**
     * Resumo curto da ação, sem dados sensíveis.
     */
    private String summary;

    /**
     * Resultado da ação (ex: "PROMISED_TO_PAY", "NO_RESPONSE").
     */
    private String outcome;

    /**
     * Data/hora de criação do registro.
     */
    private Instant createdAt;
}

