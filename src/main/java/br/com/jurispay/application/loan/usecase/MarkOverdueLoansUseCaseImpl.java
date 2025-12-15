package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Implementação do caso de uso para marcar empréstimos em atraso como OVERDUE.
 */
@Service
public class MarkOverdueLoansUseCaseImpl implements MarkOverdueLoansUseCase {

    private static final Logger log = LoggerFactory.getLogger(MarkOverdueLoansUseCaseImpl.class);

    private final LoanRepository loanRepository;

    public MarkOverdueLoansUseCaseImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public int markOverdue() {
        // Buscar empréstimos com status OPEN
        var openLoans = loanRepository.findByStatus(LoanStatus.OPEN);

        Instant now = Instant.now();
        int updatedCount = 0;

        // Para cada empréstimo aberto, verificar se está em atraso
        for (Loan loan : openLoans) {
            if (loan.getDataPrevistaDevolucao() != null
                    && loan.getDataPrevistaDevolucao().isBefore(now)) {

                // Atualizar status para OVERDUE usando método do domínio
                Loan updatedLoan = loan.markAsOverdue();
                loanRepository.save(updatedLoan);
                updatedCount++;

                log.debug("Loan {} marked as OVERDUE", loan.getId());
            }
        }

        return updatedCount;
    }
}

