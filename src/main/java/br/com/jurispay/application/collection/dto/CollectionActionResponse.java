package br.com.jurispay.application.collection.dto;

import br.com.jurispay.domain.collection.model.CollectionChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Resposta padrão de ação de cobrança.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionActionResponse {

    private Long id;

    private Long loanId;

    private Instant actionAt;

    private CollectionChannel channel;

    private String summary;

    private String outcome;

    private Instant createdAt;
}

