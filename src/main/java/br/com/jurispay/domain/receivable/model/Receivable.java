package br.com.jurispay.domain.receivable.model;

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
public class Receivable {

    private Long id;
    private Long loanId;
    private int installmentNumber;
    private Instant dueDate;
    private BigDecimal amount;
    private ReceivableStatus status;
    private Instant createdAt;
    private Instant paidAt;
}
