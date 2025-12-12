package br.com.jurispay.api.dto.creditanalysis;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de requisição para iniciar análise de crédito.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StartCreditAnalysisRequest {

    @NotNull(message = "ID do cliente é obrigatório")
    private Long customerId;

    @NotNull(message = "ID do analista é obrigatório")
    private Long analystUserId;
}

