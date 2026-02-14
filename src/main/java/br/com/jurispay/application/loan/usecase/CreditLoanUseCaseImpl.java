package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.application.loan.assembler.LoanResponseAssembler;
import br.com.jurispay.application.loan.dto.LoanResponse;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanPaymentPeriod;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import br.com.jurispay.domain.receivable.model.Receivable;
import br.com.jurispay.domain.receivable.model.ReceivableStatus;
import br.com.jurispay.domain.receivable.repository.ReceivableRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreditLoanUseCaseImpl implements CreditLoanUseCase {

    private final LoanRepository loanRepository;
    private final ReceivableRepository receivableRepository;
    private final LoanResponseAssembler responseAssembler;

    public CreditLoanUseCaseImpl(
            LoanRepository loanRepository,
            ReceivableRepository receivableRepository,
            LoanResponseAssembler responseAssembler) {
        this.loanRepository = loanRepository;
        this.receivableRepository = receivableRepository;
        this.responseAssembler = responseAssembler;
    }

    @Override
    public LoanResponse credit(Long loanId) {
        if (loanId == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "ID do empréstimo é obrigatório.");
        }

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.LOAN_NOT_FOUND, "Empréstimo não encontrado."));

        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Somente empréstimos APPROVED podem ser creditados.");
        }

        if (loan.getPeriodoPagamento() == null || loan.getQuantidadeParcelas() == null || loan.getValorParcela() == null) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Plano de pagamento do empréstimo está incompleto.");
        }

        if (loan.getPeriodoPagamento() != LoanPaymentPeriod.DAILY) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Somente empréstimos DAILY estão suportados no momento.");
        }

        Instant now = Instant.now();
        Loan creditedLoan = loan.credit(now);
        Loan savedLoan = loanRepository.save(creditedLoan);

        List<Receivable> receivables = generateDailyReceivables(savedLoan, now);
        receivableRepository.saveAll(receivables);

        return responseAssembler.toResponse(savedLoan);
    }

    private List<Receivable> generateDailyReceivables(Loan loan, Instant now) {
        int installments = loan.getQuantidadeParcelas();

        LocalDate start = LocalDate.ofInstant(now, ZoneOffset.UTC).plusDays(1);

        List<Receivable> receivables = new ArrayList<>(installments);
        for (int i = 1; i <= installments; i++) {
            Instant dueDate = start.plusDays(i - 1).atStartOfDay(ZoneOffset.UTC).toInstant();
            receivables.add(Receivable.builder()
                    .loanId(loan.getId())
                    .installmentNumber(i)
                    .dueDate(dueDate)
                    .amount(loan.getValorParcela())
                    .status(ReceivableStatus.PENDING)
                    .createdAt(now)
                    .paidAt(null)
                    .build());
        }
        return receivables;
    }
}
