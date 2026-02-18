package br.com.jurispay.application.creditanalysis.usecase;

import br.com.jurispay.application.creditcheck.usecase.RunCreditCheckUseCase;
import br.com.jurispay.application.creditcheck.usecase.GetLatestCreditCheckByLoanUseCase;
import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisDecisionCommand;
import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;
import br.com.jurispay.application.creditanalysis.mapper.CreditAnalysisApplicationMapper;
import br.com.jurispay.application.creditanalysis.service.CreditAnalysisDecisionValidator;
import br.com.jurispay.application.customer.service.CustomerKycService;
import br.com.jurispay.domain.creditcheck.model.CreditCheckDecision;
import br.com.jurispay.domain.creditcheck.model.CreditCheckStatus;
import br.com.jurispay.domain.creditdecisionoverride.model.CreditDecisionOverride;
import br.com.jurispay.domain.creditdecisionoverride.repository.CreditDecisionOverrideRepository;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysis;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysisStatus;
import br.com.jurispay.domain.creditanalysis.repository.CreditAnalysisRepository;
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

/**
 * Implementação do use case de decisão de análise de crédito.
 */
@Service
public class DecideCreditAnalysisUseCaseImpl implements DecideCreditAnalysisUseCase {

    private final CreditAnalysisRepository creditAnalysisRepository;
    private final LoanRepository loanRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final CreditAnalysisDecisionValidator decisionValidator;
    private final CreditAnalysisApplicationMapper mapper;
    private final CustomerKycService customerKycService;
    private final InstallmentScheduleService scheduleService;
    private final CustomerRepository customerRepository;
    private final RunCreditCheckUseCase runCreditCheckUseCase;
    private final GetLatestCreditCheckByLoanUseCase getLatestCreditCheckByLoanUseCase;
    private final CreditDecisionOverrideRepository creditDecisionOverrideRepository;

    public DecideCreditAnalysisUseCaseImpl(
            CreditAnalysisRepository creditAnalysisRepository,
            LoanRepository loanRepository,
            LoanTypeRepository loanTypeRepository,
            CreditAnalysisDecisionValidator decisionValidator,
            CreditAnalysisApplicationMapper mapper,
            CustomerKycService customerKycService,
            InstallmentScheduleService scheduleService,
            CustomerRepository customerRepository,
            RunCreditCheckUseCase runCreditCheckUseCase,
            GetLatestCreditCheckByLoanUseCase getLatestCreditCheckByLoanUseCase,
            CreditDecisionOverrideRepository creditDecisionOverrideRepository) {
        this.creditAnalysisRepository = creditAnalysisRepository;
        this.loanRepository = loanRepository;
        this.loanTypeRepository = loanTypeRepository;
        this.decisionValidator = decisionValidator;
        this.mapper = mapper;
        this.customerKycService = customerKycService;
        this.scheduleService = scheduleService;
        this.customerRepository = customerRepository;
        this.runCreditCheckUseCase = runCreditCheckUseCase;
        this.getLatestCreditCheckByLoanUseCase = getLatestCreditCheckByLoanUseCase;
        this.creditDecisionOverrideRepository = creditDecisionOverrideRepository;
    }

    @Override
    @Transactional
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
        Loan updatedLoan;
        if (command.getDecisionStatus() == CreditAnalysisStatus.APPROVED) {
            String cpf = customerRepository.findById(loan.getCustomerId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND, "Cliente não encontrado."))
                    .getCpf();

            runCreditCheckUseCase.run(loan.getId(), loan.getCustomerId(), cpf, command.getAnalystUserId());

            var creditCheckSummaryOpt = getLatestCreditCheckByLoanUseCase.getByLoanId(loan.getId());
            if (creditCheckSummaryOpt.isEmpty()) {
                throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Credit check não encontrado para o empréstimo. Não é possível aprovar sem override.");
            }

            var creditCheckSummary = creditCheckSummaryOpt.get();
            boolean requiresOverride = creditCheckSummary.getStatus() != CreditCheckStatus.COMPLETED
                    || creditCheckSummary.getDecision() == CreditCheckDecision.REJECTED;

            if (requiresOverride) {
                boolean override = Boolean.TRUE.equals(command.getOverrideCreditCheck());
                if (!override) {
                    throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Credit check reprovado/inconclusivo. Para aprovar é necessário override com motivo.");
                }

                String reason = command.getOverrideReason();
                if (reason == null || reason.isBlank()) {
                    throw new ValidationException(ErrorCode.REQUIRED_FIELD, "Motivo do override é obrigatório.");
                }

                creditDecisionOverrideRepository.save(CreditDecisionOverride.builder()
                        .loanId(loan.getId())
                        .creditCheckId(creditCheckSummary.getId())
                        .overrideByUserId(command.getAnalystUserId())
                        .overrideReason(reason)
                        .createdAt(now)
                        .build());
            }

            updatedLoan = approveLoanWithRepaymentPlan(loan, now);
        } else {
            updatedLoan = loan.reject(now);
        }

        loanRepository.save(updatedLoan);

        // Retornar response
        CreditAnalysisResponse response = mapper.toResponse(savedAnalysis);
        return enrichWithCreditCheckSummary(response);
    }

    private CreditAnalysisResponse enrichWithCreditCheckSummary(CreditAnalysisResponse response) {
        if (response == null || response.getLoanId() == null) {
            return response;
        }

        return CreditAnalysisResponse.builder()
                .id(response.getId())
                .loanId(response.getLoanId())
                .customerId(response.getCustomerId())
                .status(response.getStatus())
                .analystUserId(response.getAnalystUserId())
                .startedAt(response.getStartedAt())
                .finishedAt(response.getFinishedAt())
                .decisionDeadlineAt(response.getDecisionDeadlineAt())
                .rejectionReason(response.getRejectionReason())
                .notes(response.getNotes())
                .creditCheckSummary(getLatestCreditCheckByLoanUseCase.getByLoanId(response.getLoanId()).orElse(null))
                .build();
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

