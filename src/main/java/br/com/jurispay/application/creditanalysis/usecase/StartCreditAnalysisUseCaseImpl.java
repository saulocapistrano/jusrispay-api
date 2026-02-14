package br.com.jurispay.application.creditanalysis.usecase;

import br.com.jurispay.application.creditanalysis.dto.CreditAnalysisResponse;
import br.com.jurispay.application.creditanalysis.dto.StartCreditAnalysisCommand;
import br.com.jurispay.application.creditanalysis.mapper.CreditAnalysisApplicationMapper;
import br.com.jurispay.application.creditanalysis.validator.StartCreditAnalysisCommandValidator;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysis;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysisStatus;
import br.com.jurispay.domain.creditanalysis.repository.CreditAnalysisRepository;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Implementação do use case de início de análise de crédito.
 */
@Service
public class StartCreditAnalysisUseCaseImpl implements StartCreditAnalysisUseCase {

    private final CreditAnalysisRepository creditAnalysisRepository;
    private final LoanRepository loanRepository;
    private final CreditAnalysisApplicationMapper mapper;
    private final StartCreditAnalysisCommandValidator validator;

    public StartCreditAnalysisUseCaseImpl(
            CreditAnalysisRepository creditAnalysisRepository,
            LoanRepository loanRepository,
            CreditAnalysisApplicationMapper mapper,
            StartCreditAnalysisCommandValidator validator) {
        this.creditAnalysisRepository = creditAnalysisRepository;
        this.loanRepository = loanRepository;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public CreditAnalysisResponse start(StartCreditAnalysisCommand command) {
        // Validações básicas
        validator.validate(command);

        Loan loan = loanRepository.findById(command.getLoanId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.LOAN_NOT_FOUND, "Empréstimo não encontrado para iniciar análise."));

        if (loan.getStatus() != LoanStatus.REQUESTED) {
            throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Somente empréstimos solicitados podem entrar em análise.");
        }

        CreditAnalysis existingAnalysis = creditAnalysisRepository.findByLoanId(command.getLoanId())
                .orElse(null);

        CreditAnalysis analysis;

        if (existingAnalysis != null) {
            CreditAnalysisStatus currentStatus = existingAnalysis.getStatus();

            if (currentStatus == CreditAnalysisStatus.APPROVED) {
                throw new ValidationException(ErrorCode.BUSINESS_RULE_VIOLATION, "Análise já aprovada.");
            }

            if (currentStatus == CreditAnalysisStatus.IN_REVIEW) {
                throw new ValidationException(ErrorCode.ANALYSIS_ALREADY_IN_REVIEW, "Análise já está em andamento.");
            }

            // Se REJECTED ou PENDING, permitir reiniciar
            Instant now = Instant.now();
            analysis = CreditAnalysis.builder()
                    .id(existingAnalysis.getId())
                    .loanId(existingAnalysis.getLoanId())
                    .customerId(existingAnalysis.getCustomerId())
                    .status(CreditAnalysisStatus.IN_REVIEW)
                    .analystUserId(command.getAnalystUserId())
                    .startedAt(now)
                    .decisionDeadlineAt(now.plus(24, ChronoUnit.HOURS))
                    .rejectionReason(null)
                    .notes(null)
                    .createdAt(existingAnalysis.getCreatedAt())
                    .updatedAt(now)
                    .build();
        } else {
            // Criar nova análise
            Instant now = Instant.now();
            analysis = CreditAnalysis.builder()
                    .loanId(command.getLoanId())
                    .customerId(loan.getCustomerId())
                    .status(CreditAnalysisStatus.IN_REVIEW)
                    .analystUserId(command.getAnalystUserId())
                    .startedAt(now)
                    .decisionDeadlineAt(now.plus(24, ChronoUnit.HOURS))
                    .createdAt(now)
                    .updatedAt(now)
                    .build();
        }

        // Salvar análise
        CreditAnalysis savedAnalysis = creditAnalysisRepository.save(analysis);

        // Retornar response
        return mapper.toResponse(savedAnalysis);
    }
}

