package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;

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
        // Buscar empréstimos ativos (OPEN legado + CREDITED)
        var activeLoans = new ArrayList<Loan>();
        activeLoans.addAll(loanRepository.findByStatus(LoanStatus.OPEN));
        activeLoans.addAll(loanRepository.findByStatus(LoanStatus.CREDITED));

        Instant now = Instant.now();
        int updatedCount = 0;

        // Para cada empréstimo aberto, verificar se está em atraso
        for (Loan loan : activeLoans) {
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

