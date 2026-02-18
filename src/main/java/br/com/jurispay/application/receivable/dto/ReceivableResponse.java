package br.com.jurispay.application.receivable.dto;

import br.com.jurispay.domain.receivable.model.ReceivableStatus;
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
public class ReceivableResponse {

    private Long id;
    private Long loanId;
    private int installmentNumber;
    private Instant dueDate;
    private BigDecimal amount;
    private ReceivableStatus status;
    private Instant paidAt;
}
