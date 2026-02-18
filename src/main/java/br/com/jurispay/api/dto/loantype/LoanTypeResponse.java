package br.com.jurispay.api.dto.loantype;

import lombok.*;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanTypeResponse {

    private Long id;
    private String nome;
    private String descricao;
    private Integer intervaloPagamentoDias;
    private String scheduleType;
    private String weeklyDayOfWeek;
    private Boolean ativo;
    private Instant dataCriacao;
    private Instant dataAtualizacao;
}
