package br.com.jurispay.infrastructure.config.scheduler;

import br.com.jurispay.application.loan.usecase.MarkOverdueLoansUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler para marcar empréstimos em atraso como OVERDUE.
 */
@Component
public class OverdueLoanScheduler {

    private static final Logger log = LoggerFactory.getLogger(OverdueLoanScheduler.class);

    private final MarkOverdueLoansUseCase markOverdueLoansUseCase;

    public OverdueLoanScheduler(MarkOverdueLoansUseCase markOverdueLoansUseCase) {
        this.markOverdueLoansUseCase = markOverdueLoansUseCase;
    }

    @Scheduled(fixedDelayString = "${jurispay.scheduler.overdue.fixed-delay-ms:60000}")
    public void markOverdueLoans() {
        int updatedCount = markOverdueLoansUseCase.markOverdue();
        if (updatedCount > 0) {
            log.info("Overdue scheduler updated {} loans", updatedCount);
        }
    }
}

