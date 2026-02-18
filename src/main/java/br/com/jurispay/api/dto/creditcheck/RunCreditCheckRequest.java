package br.com.jurispay.api.dto.creditcheck;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RunCreditCheckRequest {

    @NotNull(message = "ID do usuário requisitante é obrigatório")
    private Long requestedByUserId;
}
