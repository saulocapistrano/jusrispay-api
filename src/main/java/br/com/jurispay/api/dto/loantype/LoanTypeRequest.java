package br.com.jurispay.api.dto.loantype;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanTypeRequest {

    @NotBlank
    private String nome;

    private String descricao;

    @NotNull
    @Min(1)
    private Integer intervaloPagamentoDias;

    @NotNull
    private String scheduleType;

    private String weeklyDayOfWeek;

    @NotNull
    private Boolean ativo;
}
