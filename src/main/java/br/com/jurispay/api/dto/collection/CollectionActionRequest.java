package br.com.jurispay.api.dto.collection;

import br.com.jurispay.domain.collection.model.CollectionChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de requisição para registro de ação de cobrança.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionActionRequest {

    @NotNull(message = "ID do empréstimo é obrigatório")
    private Long loanId;

    @NotNull(message = "Canal de cobrança é obrigatório")
    private CollectionChannel channel;

    @NotBlank(message = "Resumo da ação é obrigatório")
    @Size(max = 300, message = "Resumo da ação não pode exceder 300 caracteres")
    private String summary;

    @Size(max = 60, message = "Resultado da ação não pode exceder 60 caracteres")
    private String outcome;
}

