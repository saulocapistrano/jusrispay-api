package br.com.jurispay.application.receivable.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayReceivableCommand {

    private Long receivableId;
    private Boolean adimplente;

    private ReceivablePaymentType paymentType;

    private Long fineId;
    private Integer fineTimes;
}
