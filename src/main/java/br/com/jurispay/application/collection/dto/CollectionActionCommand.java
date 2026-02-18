package br.com.jurispay.application.collection.dto;

import br.com.jurispay.domain.collection.model.CollectionChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Comando para registro de ação de cobrança.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionActionCommand {

    private Long loanId;

    private CollectionChannel channel;

    private String summary;

    private String outcome;
}

