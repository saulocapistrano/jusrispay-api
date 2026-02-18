package br.com.jurispay.api.dto.fine;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FineRequest {

    @NotBlank
    private String nome;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal valor;

    @NotNull
    private Boolean ativo;
}
