package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.application.loan.dto.LoanCreationCommand;
import br.com.jurispay.application.loan.dto.LoanResponse;
import br.com.jurispay.application.loan.mapper.LoanApplicationMapper;
import br.com.jurispay.application.loan.validator.LoanCreationCommandValidator;
import br.com.jurispay.domain.common.exception.ErrorCode;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysisStatus;
import br.com.jurispay.domain.creditanalysis.repository.CreditAnalysisRepository;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import br.com.jurispay.domain.loan.factory.LoanCreationData;
import br.com.jurispay.domain.loan.factory.LoanFactory;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.policy.LoanPolicy;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Implementação do caso de uso de criação de empréstimo.
 */
@Service
public class CreateLoanUseCaseImpl implements CreateLoanUseCase {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final CreditAnalysisRepository creditAnalysisRepository;
    private final LoanApplicationMapper mapper;
    private final LoanCreationCommandValidator validator;
    private final LoanFactory loanFactory;
    private final LoanPolicy loanPolicy;

    public CreateLoanUseCaseImpl(
            LoanRepository loanRepository,
            CustomerRepository customerRepository,
            CreditAnalysisRepository creditAnalysisRepository,
            LoanApplicationMapper mapper,
            LoanCreationCommandValidator validator,
            LoanFactory loanFactory,
            LoanPolicy loanPolicy) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.creditAnalysisRepository = creditAnalysisRepository;
        this.mapper = mapper;
        this.validator = validator;
        this.loanFactory = loanFactory;
        this.loanPolicy = loanPolicy;
    }

    @Override
    public LoanResponse create(LoanCreationCommand command) {
        // Validações de negócio
        validator.validate(command);

        // Verificar se cliente existe
        customerRepository.findById(command.getCustomerId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND, "Cliente não encontrado para criação de empréstimo."));

        // Verificar se cliente está aprovado na análise de crédito
        creditAnalysisRepository.findByCustomerId(command.getCustomerId())
                .filter(analysis -> analysis.getStatus() == CreditAnalysisStatus.APPROVED)
                .orElseThrow(() -> new ValidationException(ErrorCode.CUSTOMER_NOT_APPROVED, "Cliente não aprovado na análise de crédito."));

        // Montar dados de criação
        LoanCreationData creationData = LoanCreationData.builder()
                .customerId(command.getCustomerId())
                .valorSolicitado(command.getValorSolicitado())
                .dataPrevistaDevolucao(command.getDataPrevistaDevolucao())
                .build();

        // Criar Loan usando factory
        Instant now = Instant.now();
        Loan loan = loanFactory.create(creationData, loanPolicy, now);

        // Salvar empréstimo
        Loan savedLoan = loanRepository.save(loan);

        // Retornar resposta
        return mapper.toResponse(savedLoan);
    }
}

