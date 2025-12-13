package br.com.jurispay.application.collection.usecase;

import br.com.jurispay.application.collection.dto.OverdueInfoResponse;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.collection.model.OverdueInfo;
import br.com.jurispay.domain.collection.service.OverdueCalculator;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Implementação do caso de uso de obtenção de informações de inadimplência.
 */
@Service
public class GetOverdueInfoUseCaseImpl implements GetOverdueInfoUseCase {

    private final LoanRepository loanRepository;
    private final OverdueCalculator overdueCalculator;

    public GetOverdueInfoUseCaseImpl(
            LoanRepository loanRepository,
            OverdueCalculator overdueCalculator) {
        this.loanRepository = loanRepository;
        this.overdueCalculator = overdueCalculator;
    }

    @Override
    public OverdueInfoResponse getOverdueInfo(Long loanId) {
        // Buscar empréstimo
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException("Empréstimo não encontrado."));

        // Obter dados do empréstimo
        Instant dueDate = loan.getDataPrevistaDevolucao();
        BigDecimal dailyFine = loan.getMultaDiaria();

        // Calcular agora
        Instant now = Instant.now();

        // Calcular inadimplência
        OverdueInfo overdueInfo = overdueCalculator.calculate(dueDate, now, dailyFine);

        // Montar resposta
        return OverdueInfoResponse.builder()
                .loanId(loanId)
                .daysOverdue(overdueInfo.getDaysOverdue())
                .dailyFine(dailyFine)
                .totalFine(overdueInfo.getTotalFine())
                .dueDate(dueDate)
                .calculatedAt(now)
                .build();
    }
}

