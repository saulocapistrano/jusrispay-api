package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.application.customer.service.CustomerKycService;
import br.com.jurispay.application.loan.assembler.LoanResponseAssembler;
import br.com.jurispay.application.loan.dto.LoanResponse;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysis;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysisStatus;
import br.com.jurispay.domain.creditanalysis.repository.CreditAnalysisRepository;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import br.com.jurispay.domain.loan.service.InstallmentScheduleService;
import br.com.jurispay.domain.loantype.model.LoanType;
import br.com.jurispay.domain.loantype.repository.LoanTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Service
public class SyncLoanStatusFromCreditAnalysisUseCaseImpl implements SyncLoanStatusFromCreditAnalysisUseCase {

    private final LoanRepository loanRepository;
    private final CreditAnalysisRepository creditAnalysisRepository;
    private final CustomerKycService customerKycService;
    private final LoanTypeRepository loanTypeRepository;
    private final InstallmentScheduleService scheduleService;
    private final LoanResponseAssembler responseAssembler;

    public SyncLoanStatusFromCreditAnalysisUseCaseImpl(
            LoanRepository loanRepository,
            CreditAnalysisRepository creditAnalysisRepository,
            CustomerKycService customerKycService,
            LoanTypeRepository loanTypeRepository,
            InstallmentScheduleService scheduleService,
            LoanResponseAssembler responseAssembler) {
        this.loanRepository = loanRepository;
        this.creditAnalysisRepository = creditAnalysisRepository;
        this.customerKycService = customerKycService;
        this.loanTypeRepository = loanTypeRepository;
        this.scheduleService = scheduleService;
        this.responseAssembler = responseAssembler;
    }

    @Override
    @Transactional
    public LoanResponse sync(Long loanId) {
        if (loanId == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "ID do empréstimo é obrigatório.");
        }

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.LOAN_NOT_FOUND, "Empréstimo não encontrado."));

        CreditAnalysis analysis = creditAnalysisRepository.findByLoanId(loanId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.CREDIT_ANALYSIS_NOT_FOUND, "Análise de crédito não encontrada para o empréstimo."));

        CreditAnalysisStatus status = analysis.getStatus();
        if (status == null || status == CreditAnalysisStatus.IN_REVIEW) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Análise não está finalizada.");
        }

        Loan updatedLoan;
        Instant now = Instant.now();

        if (status == CreditAnalysisStatus.APPROVED) {
            if (loan.getStatus() == LoanStatus.APPROVED || loan.getStatus() == LoanStatus.CREDITED || loan.getStatus() == LoanStatus.PAID || loan.getStatus() == LoanStatus.OVERDUE) {
                return responseAssembler.toResponse(loan);
            }

            if (loan.getStatus() != LoanStatus.REQUESTED) {
                throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Sincronização só é permitida quando o empréstimo está REQUESTED.");
            }

            customerKycService.validateCustomerEligibleForLoan(loan.getCustomerId(), now);
            updatedLoan = approveLoanWithRepaymentPlan(loan, now);
        } else if (status == CreditAnalysisStatus.REJECTED) {
            if (loan.getStatus() == LoanStatus.REJECTED || loan.getStatus() == LoanStatus.CANCELED) {
                return responseAssembler.toResponse(loan);
            }

            if (loan.getStatus() != LoanStatus.REQUESTED) {
                throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Sincronização só é permitida quando o empréstimo está REQUESTED.");
            }

            updatedLoan = loan.reject(now);
        } else {
            throw new ValidationException(ErrorCode.INVALID_VALUE, "Status de análise inválido para sincronização.");
        }

        Loan saved = loanRepository.save(updatedLoan);
        return responseAssembler.toResponse(saved);
    }

    private Loan approveLoanWithRepaymentPlan(Loan loan, Instant now) {
        if (loan.getLoanTypeId() == null) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Tipo de empréstimo (loanTypeId) não informado.");
        }

        LoanType loanType = loanTypeRepository.findById(loan.getLoanTypeId())
                .orElseThrow(() -> new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Tipo de empréstimo não encontrado."));

        if (loan.getDataPrevistaDevolucao() == null) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Data prevista de devolução do empréstimo não informada.");
        }

        LocalDate startDate = LocalDate.ofInstant(now, ZoneOffset.UTC).plusDays(1);
        LocalDate dueDate = LocalDate.ofInstant(loan.getDataPrevistaDevolucao(), ZoneOffset.UTC);

        int installments = scheduleService.calculateInstallments(loanType, startDate, dueDate);
        if (installments < 1) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Quantidade de parcelas inválida.");
        }

        BigDecimal valorParcela = loan.getValorDevolucaoPrevista()
                .divide(BigDecimal.valueOf(installments), 2, RoundingMode.HALF_UP);

        Loan loanWithPlan = loan.applyRepaymentPlan(scheduleService.resolvePaymentPeriod(loanType), installments, valorParcela);
        return loanWithPlan.approve(now);
    }
}
