package br.com.jurispay.api.dto.fine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FineResponse {

    private Long id;
    private String nome;
    private BigDecimal valor;
    private Boolean ativo;
    private Instant dataCriacao;
    private Instant dataAtualizacao;
}
