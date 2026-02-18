package br.com.jurispay.domain.loantype.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanType {

    private Long id;
    private String nome;
    private String descricao;
    private Integer intervaloPagamentoDias;
    private LoanTypeScheduleType scheduleType;
    private String weeklyDayOfWeek;
    private Boolean ativo;
    private Instant dataCriacao;
    private Instant dataAtualizacao;
}
