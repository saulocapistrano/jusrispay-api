package br.com.jurispay.application.receivable.usecase;

import br.com.jurispay.application.receivable.dto.PayReceivableCommand;
import br.com.jurispay.application.receivable.dto.ReceivablePaymentType;
import br.com.jurispay.application.receivable.dto.ReceivableResponse;
import br.com.jurispay.application.receivable.mapper.ReceivableApplicationMapper;
import br.com.jurispay.domain.collection.model.OverdueInfo;
import br.com.jurispay.domain.collection.service.OverdueCalculator;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.fine.model.Fine;
import br.com.jurispay.domain.fine.repository.FineRepository;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanPaymentPeriod;
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
import java.math.RoundingMode;
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
    private final FineRepository fineRepository;
    private final WalletRepository walletRepository;
    private final OverdueCalculator overdueCalculator;
    private final RoiCalculator roiCalculator;
    private final ReceivableApplicationMapper mapper;

    public PayReceivableUseCaseImpl(
            ReceivableRepository receivableRepository,
            LoanRepository loanRepository,
            PaymentRepository paymentRepository,
            FineRepository fineRepository,
            WalletRepository walletRepository,
            OverdueCalculator overdueCalculator,
            RoiCalculator roiCalculator,
            ReceivableApplicationMapper mapper) {
        this.receivableRepository = receivableRepository;
        this.loanRepository = loanRepository;
        this.paymentRepository = paymentRepository;
        this.fineRepository = fineRepository;
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

        Loan loan = loanRepository.findById(receivable.getLoanId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.LOAN_NOT_FOUND, "Empréstimo não encontrado."));

        LoanPaymentPeriod periodoPagamento = loan.getPeriodoPagamento();
        boolean isDaily = periodoPagamento == null || periodoPagamento == LoanPaymentPeriod.DAILY;

        ReceivablePaymentType paymentType = command.getPaymentType();
        if (isDaily) {
            paymentType = ReceivablePaymentType.SETTLE_TOTAL;
        } else if (paymentType == null) {
            paymentType = ReceivablePaymentType.SETTLE_TOTAL;
        }

        BigDecimal paidBaseAmount = resolvePaidBaseAmount(receivable, loan, paymentType);

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

        registerPaymentForReceivable(saved, loan, now, paidBaseAmount, command);

        if (paymentType == ReceivablePaymentType.PAY_INTEREST_ONLY) {
            createRenewedReceivable(saved, loan, now);
            return mapper.toResponse(saved);
        }

        if (isDaily) {
            ensureLoanMarkedAsPaidIfFinished(saved.getLoanId());
        } else {
            loanRepository.save(loan.markAsPaid());
        }

        return mapper.toResponse(saved);
    }

    private void registerPaymentForReceivable(
            Receivable receivable,
            Loan loan,
            Instant paidAt,
            BigDecimal paidBaseAmount,
            PayReceivableCommand command) {
        if (receivable == null || receivable.getLoanId() == null) {
            return;
        }

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

        Fine fine = null;
        Integer fineTimes = null;
        BigDecimal fineUnitAmount = null;
        BigDecimal fineTotalAmount = null;

        if (daysOverdue > 0) {
            if (command != null && (command.getFineId() != null || command.getFineTimes() != null)) {
                if (command.getFineId() == null) {
                    throw new ValidationException(ErrorCode.REQUIRED_FIELD, "fineId é obrigatório quando fineTimes é informado.");
                }
                if (command.getFineTimes() == null || command.getFineTimes() < 1) {
                    throw new ValidationException(ErrorCode.INVALID_VALUE, "fineTimes deve ser >= 1 quando fineId é informado.");
                }

                fine = fineRepository.findById(command.getFineId())
                        .filter(f -> Boolean.TRUE.equals(f.getAtivo()))
                        .orElseThrow(() -> new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Multa não encontrada ou inativa."));

                fineTimes = command.getFineTimes();
                fineUnitAmount = fine.getValor();
                fineTotalAmount = fineUnitAmount.multiply(BigDecimal.valueOf(fineTimes)).setScale(2, RoundingMode.HALF_UP);
                totalFine = totalFine.add(fineTotalAmount);
            }
        }

        BigDecimal valorPago = paidBaseAmount != null ? paidBaseAmount : receivable.getAmount();
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
                .fineId(fine != null ? fine.getId() : null)
                .fineTimes(fineTimes)
                .fineUnitAmount(fineUnitAmount)
                .fineTotalAmount(fineTotalAmount)
                .valorFinalRecebido(valorFinalRecebido)
                .roiBrl(roiBrl)
                .roiPercent(roiPercent)
                .dataCriacao(Instant.now())
                .build();

        paymentRepository.save(payment);

        creditJurispayWallet(valorFinalRecebido, paidAt);
    }

    private BigDecimal resolvePaidBaseAmount(Receivable receivable, Loan loan, ReceivablePaymentType paymentType) {
        if (paymentType == ReceivablePaymentType.PAY_INTEREST_ONLY) {
            if (loan.getValorSolicitado() == null || loan.getTaxaJuros() == null) {
                throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Empréstimo sem valorSolicitado/taxaJuros.");
            }

            return loan.getValorSolicitado()
                    .multiply(loan.getTaxaJuros())
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return receivable.getAmount();
    }

    private void createRenewedReceivable(Receivable paidReceivable, Loan loan, Instant now) {
        if (paidReceivable.getDueDate() == null) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Parcela sem data de vencimento.");
        }
        if (loan.getPeriodoPagamento() == null || loan.getPeriodoPagamento() == LoanPaymentPeriod.DAILY) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Renovação permitida apenas para empréstimos não-diários.");
        }

        LocalDate baseDue = LocalDate.ofInstant(paidReceivable.getDueDate(), ZoneOffset.UTC);
        LocalDate nextDue = baseDue.plusDays(daysForPeriod(loan.getPeriodoPagamento()));
        Instant nextDueInstant = nextDue.atStartOfDay(ZoneOffset.UTC).toInstant();

        int nextInstallmentNumber = paidReceivable.getInstallmentNumber() + 1;

        Receivable renewed = Receivable.builder()
                .loanId(paidReceivable.getLoanId())
                .installmentNumber(nextInstallmentNumber)
                .dueDate(nextDueInstant)
                .amount(loan.getValorDevolucaoPrevista())
                .status(ReceivableStatus.PENDING)
                .createdAt(now)
                .paidAt(null)
                .build();

        receivableRepository.save(renewed);
    }

    private long daysForPeriod(LoanPaymentPeriod period) {
        if (period == null) {
            return 0;
        }
        return switch (period) {
            case DAILY -> 1;
            case WEEKLY -> 7;
            case TEN_DAYS -> 10;
            case FIFTEEN_DAYS -> 15;
            case TWENTY_DAYS -> 20;
            case MONTHLY -> 30;
        };
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
