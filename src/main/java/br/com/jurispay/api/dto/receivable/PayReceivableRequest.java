package br.com.jurispay.api.dto.receivable;

import br.com.jurispay.application.receivable.dto.ReceivablePaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayReceivableRequest {

    @NotNull(message = "adimplente é obrigatório")
    private Boolean adimplente;

    private ReceivablePaymentType paymentType;

    private Long fineId;

    private Integer fineTimes;
}
