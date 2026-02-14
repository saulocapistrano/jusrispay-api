package br.com.jurispay.application.creditanalysis.usecase;

import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisDecisionCommand;
import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;
import br.com.jurispay.application.creditanalysis.mapper.CreditAnalysisApplicationMapper;
import br.com.jurispay.application.creditanalysis.service.CreditAnalysisDecisionValidator;
import br.com.jurispay.application.customer.service.CustomerKycService;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysis;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysisStatus;
import br.com.jurispay.domain.creditanalysis.repository.CreditAnalysisRepository;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanPaymentPeriod;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * Implementação do use case de decisão de análise de crédito.
 */
@Service
public class DecideCreditAnalysisUseCaseImpl implements DecideCreditAnalysisUseCase {

    private final CreditAnalysisRepository creditAnalysisRepository;
    private final LoanRepository loanRepository;
    private final CreditAnalysisDecisionValidator decisionValidator;
    private final CreditAnalysisApplicationMapper mapper;
    private final CustomerKycService customerKycService;

    public DecideCreditAnalysisUseCaseImpl(
            CreditAnalysisRepository creditAnalysisRepository,
            LoanRepository loanRepository,
            CreditAnalysisDecisionValidator decisionValidator,
            CreditAnalysisApplicationMapper mapper,
            CustomerKycService customerKycService) {
        this.creditAnalysisRepository = creditAnalysisRepository;
        this.loanRepository = loanRepository;
        this.decisionValidator = decisionValidator;
        this.mapper = mapper;
        this.customerKycService = customerKycService;
    }

    @Override
    public CreditAnalysisResponse decide(CreditAnalysisDecisionCommand command) {
        // Validações básicas
        if (command.getLoanId() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "ID do empréstimo é obrigatório.");
        }

        if (command.getAnalystUserId() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "ID do analista é obrigatório.");
        }

        if (command.getDecisionStatus() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "Status da decisão é obrigatório.");
        }

        if (command.getDecisionStatus() != CreditAnalysisStatus.APPROVED &&
            command.getDecisionStatus() != CreditAnalysisStatus.REJECTED) {
            throw new ValidationException(ErrorCode.INVALID_VALUE, "Status da decisão deve ser APPROVED ou REJECTED.");
        }

        Loan loan = loanRepository.findById(command.getLoanId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.LOAN_NOT_FOUND, "Empréstimo não encontrado."));

        if (loan.getStatus() != LoanStatus.REQUESTED) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Somente empréstimos solicitados podem ser analisados.");
        }

        // Buscar análise existente
        CreditAnalysis analysis = creditAnalysisRepository.findByLoanId(command.getLoanId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CREDIT_ANALYSIS_NOT_FOUND, "Análise de crédito não encontrada para o empréstimo."));

        // Validar que análise está em andamento
        if (analysis.getStatus() != CreditAnalysisStatus.IN_REVIEW) {
            throw new ValidationException(ErrorCode.ANALYSIS_NOT_IN_REVIEW, "Análise não está em andamento.");
        }

        // Se aprovação, validar checklist de documentos
        if (command.getDecisionStatus() == CreditAnalysisStatus.APPROVED) {
            customerKycService.validateCustomerEligibleForLoan(loan.getCustomerId(), Instant.now());
        }

        // Atualizar análise com decisão
        Instant now = Instant.now();
        CreditAnalysis updatedAnalysis = CreditAnalysis.builder()
                .id(analysis.getId())
                .loanId(analysis.getLoanId())
                .customerId(analysis.getCustomerId())
                .status(command.getDecisionStatus())
                .analystUserId(command.getAnalystUserId())
                .startedAt(analysis.getStartedAt())
                .finishedAt(now)
                .decisionDeadlineAt(analysis.getDecisionDeadlineAt())
                .rejectionReason(command.getDecisionStatus() == CreditAnalysisStatus.REJECTED
                        ? command.getRejectionReason()
                        : null)
                .notes(command.getNotes())
                .createdAt(analysis.getCreatedAt())
                .updatedAt(now)
                .build();

        // Validar regras de decisão
        decisionValidator.validateDecision(updatedAnalysis);

        // Salvar análise
        CreditAnalysis savedAnalysis = creditAnalysisRepository.save(updatedAnalysis);

        // Aplicar decisão no empréstimo
        Loan updatedLoan = command.getDecisionStatus() == CreditAnalysisStatus.APPROVED
                ? approveLoanWithRepaymentPlan(loan, now)
                : loan.reject(now);
        loanRepository.save(updatedLoan);

        // Retornar response
        return mapper.toResponse(savedAnalysis);
    }

    private Loan approveLoanWithRepaymentPlan(Loan loan, Instant now) {
        if (loan.getPeriodoPagamento() == null) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Período de pagamento do empréstimo não informado.");
        }

        if (loan.getPeriodoPagamento() != LoanPaymentPeriod.DAILY) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Somente empréstimos DAILY estão suportados no momento.");
        }

        if (loan.getDataPrevistaDevolucao() == null) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Data prevista de devolução do empréstimo não informada.");
        }

        LocalDate startDate = LocalDate.ofInstant(now, ZoneOffset.UTC).plusDays(1);
        LocalDate dueDate = LocalDate.ofInstant(loan.getDataPrevistaDevolucao(), ZoneOffset.UTC);
        long daysInclusive = ChronoUnit.DAYS.between(startDate, dueDate) + 1;

        if (daysInclusive < 1 || daysInclusive > 25) {
            throw new ValidationException(
                    ErrorCode.BUSINESS_RULE_VIOLATION,
                    "Empréstimo DAILY deve ter entre 1 e 25 dias (de dataLiberacao + 1 até dataPrevistaDevolucao, inclusive)."
            );
        }

        BigDecimal valorParcela = loan.getValorDevolucaoPrevista()
                .divide(BigDecimal.valueOf(daysInclusive), 2, RoundingMode.HALF_UP);

        Loan loanWithPlan = loan.applyRepaymentPlan(LoanPaymentPeriod.DAILY, (int) daysInclusive, valorParcela);
        return loanWithPlan.approve(now);
    }
}

