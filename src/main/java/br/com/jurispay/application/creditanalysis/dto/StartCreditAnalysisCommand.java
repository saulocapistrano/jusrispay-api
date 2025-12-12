package br.com.jurispay.application.creditanalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Comando para iniciar uma análise de crédito.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartCreditAnalysisCommand {

    private Long customerId;

    private Long analystUserId;
}

