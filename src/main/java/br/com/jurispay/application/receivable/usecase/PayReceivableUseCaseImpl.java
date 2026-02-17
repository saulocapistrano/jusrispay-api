package br.com.jurispay.application.receivable.usecase;

import br.com.jurispay.application.receivable.dto.PayReceivableCommand;
import br.com.jurispay.application.receivable.dto.ReceivableResponse;
import br.com.jurispay.application.receivable.mapper.ReceivableApplicationMapper;
import br.com.jurispay.domain.collection.model.OverdueInfo;
import br.com.jurispay.domain.collection.service.OverdueCalculator;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import br.com.jurispay.domain.loan.service.RoiCalculator;
import br.com.jurispay.domain.payment.model.Payment;
import br.com.jurispay.domain.payment.model.PaymentMethod;
import br.com.jurispay.domain.payment.repository.PaymentRepository;
import br.com.jurispay.domain.receivable.model.Receivable;
import br.com.jurispay.domain.receivable.model.ReceivableStatus;
import br.com.jurispay.domain.receivable.repository.ReceivableRepository;
import br.com.jurispay.domain.wallet.model.Wallet;
import br.com.jurispay.domain.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PayReceivableUseCaseImpl implements PayReceivableUseCase {

    private final ReceivableRepository receivableRepository;
    private final LoanRepository loanRepository;
    private final PaymentRepository paymentRepository;
    private final WalletRepository walletRepository;
    private final OverdueCalculator overdueCalculator;
    private final RoiCalculator roiCalculator;
    private final ReceivableApplicationMapper mapper;

    public PayReceivableUseCaseImpl(
            ReceivableRepository receivableRepository,
            LoanRepository loanRepository,
            PaymentRepository paymentRepository,
            WalletRepository walletRepository,
            OverdueCalculator overdueCalculator,
            RoiCalculator roiCalculator,
            ReceivableApplicationMapper mapper) {
        this.receivableRepository = receivableRepository;
        this.loanRepository = loanRepository;
        this.paymentRepository = paymentRepository;
        this.walletRepository = walletRepository;
        this.overdueCalculator = overdueCalculator;
        this.roiCalculator = roiCalculator;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public ReceivableResponse pay(PayReceivableCommand command) {
        if (command == null || command.getReceivableId() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "receivableId é obrigatório.");
        }

        Receivable receivable = receivableRepository.findById(command.getReceivableId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.RECEIVABLE_NOT_FOUND, "Parcela não encontrada."));

        if (receivable.getStatus() != ReceivableStatus.PENDING) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Somente parcelas PENDING podem ser pagas.");
        }

        Instant now = Instant.now();
        boolean adimplente = Boolean.TRUE.equals(command.getAdimplente());

        Receivable updated = Receivable.builder()
                .id(receivable.getId())
                .loanId(receivable.getLoanId())
                .installmentNumber(receivable.getInstallmentNumber())
                .dueDate(receivable.getDueDate())
                .amount(receivable.getAmount())
                .createdAt(receivable.getCreatedAt())
                .status(adimplente ? ReceivableStatus.PAID : ReceivableStatus.OVERDUE_PAID)
                .paidAt(now)
                .build();

        Receivable saved = receivableRepository.save(updated);

        registerPaymentForReceivable(saved, now);

        ensureLoanMarkedAsPaidIfFinished(saved.getLoanId());

        return mapper.toResponse(saved);
    }

    private void registerPaymentForReceivable(Receivable receivable, Instant paidAt) {
        if (receivable == null || receivable.getLoanId() == null) {
            return;
        }

        Loan loan = loanRepository.findById(receivable.getLoanId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.LOAN_NOT_FOUND, "Empréstimo não encontrado."));

        Instant dueDate = receivable.getDueDate();
        if (dueDate == null) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Parcela sem data de vencimento.");
        }

        long daysOverdue = Math.max(0, ChronoUnit.DAYS.between(
                LocalDate.ofInstant(dueDate, ZoneOffset.UTC),
                LocalDate.ofInstant(paidAt, ZoneOffset.UTC)
        ));

        BigDecimal multaDiaria = loan.getMultaDiaria() != null
                ? loan.getMultaDiaria()
                : BigDecimal.ZERO;

        OverdueInfo overdueInfo = overdueCalculator.calculate(dueDate, paidAt, multaDiaria);

        BigDecimal totalFine = overdueInfo != null && overdueInfo.getTotalFine() != null
                ? overdueInfo.getTotalFine()
                : BigDecimal.ZERO;

        BigDecimal valorPago = receivable.getAmount();
        BigDecimal valorFinalRecebido = valorPago;
        valorFinalRecebido = valorFinalRecebido.add(totalFine);

        BigDecimal roiBrl = roiCalculator.calculateRoiBrl(valorFinalRecebido, loan.getValorSolicitado());
        BigDecimal roiPercent = roiCalculator.calculateRoiPercent(roiBrl, loan.getValorSolicitado());

        Payment payment = Payment.builder()
                .loanId(loan.getId())
                .valorPago(valorPago)
                .dataPagamento(paidAt)
                .metodo(PaymentMethod.CASH)
                .diasAtraso((int) daysOverdue)
                .multaTotal(totalFine)
                .valorFinalRecebido(valorFinalRecebido)
                .roiBrl(roiBrl)
                .roiPercent(roiPercent)
                .dataCriacao(Instant.now())
                .build();

        paymentRepository.save(payment);

        creditJurispayWallet(valorFinalRecebido, paidAt);
    }

    private void creditJurispayWallet(BigDecimal amount, Instant now) {
        if (amount == null) {
            return;
        }

        Wallet wallet = walletRepository.findForUpdateByOwner("JURISPAY", 1L)
                .orElseThrow(() -> new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Wallet Jurispay não encontrada."));

        BigDecimal current = wallet.getSaldo() != null ? wallet.getSaldo() : BigDecimal.ZERO;
        BigDecimal next = current.add(amount);

        Wallet updated = Wallet.builder()
                .id(wallet.getId())
                .ownerType(wallet.getOwnerType())
                .ownerId(wallet.getOwnerId())
                .saldo(next)
                .dataCriacao(wallet.getDataCriacao())
                .dataAtualizacao(now)
                .build();

        walletRepository.save(updated);
    }

    private void ensureLoanMarkedAsPaidIfFinished(Long loanId) {
        if (loanId == null) {
            return;
        }

        List<Receivable> receivables = receivableRepository.findByLoanId(loanId);
        boolean allPaid = receivables != null && !receivables.isEmpty() && receivables.stream()
                .allMatch(r -> r.getStatus() == ReceivableStatus.PAID || r.getStatus() == ReceivableStatus.OVERDUE_PAID);

        if (!allPaid) {
            return;
        }

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.LOAN_NOT_FOUND, "Empréstimo não encontrado."));

        if (loan.getStatus() == LoanStatus.PAID) {
            return;
        }

        loanRepository.save(loan.markAsPaid());
    }
}
