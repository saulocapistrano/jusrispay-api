package br.com.jurispay.application.notification.port;

import java.math.BigDecimal;
import java.time.Instant;

public record ReceivableNotificationCandidate(
        Long receivableId,
        Long loanId,
        Long customerId,
        String customerName,
        String customerCellphone,
        String customerPhone,
        Integer installmentNumber,
        Integer totalInstallments,
        BigDecimal amount,
        Instant dueDate
) {
}
