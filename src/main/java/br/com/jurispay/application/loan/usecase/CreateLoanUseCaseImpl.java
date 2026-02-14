package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.application.loan.dto.LoanCreationCommand;
import br.com.jurispay.application.loan.dto.LoanResponse;
import br.com.jurispay.application.loan.assembler.LoanResponseAssembler;
import br.com.jurispay.application.loan.validator.LoanCreationCommandValidator;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.application.customer.service.CustomerKycService;
import br.com.jurispay.domain.exception.common.ErrorCode;
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
    private final LoanResponseAssembler responseAssembler;
    private final LoanCreationCommandValidator validator;
    private final LoanFactory loanFactory;
    private final LoanPolicy loanPolicy;
    private final CustomerKycService customerKycService;

    public CreateLoanUseCaseImpl(
            LoanRepository loanRepository,
            CustomerRepository customerRepository,
            LoanResponseAssembler responseAssembler,
            LoanCreationCommandValidator validator,
            LoanFactory loanFactory,
            LoanPolicy loanPolicy,
            CustomerKycService customerKycService) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.responseAssembler = responseAssembler;
        this.validator = validator;
        this.loanFactory = loanFactory;
        this.loanPolicy = loanPolicy;
        this.customerKycService = customerKycService;
    }

    @Override
    public LoanResponse create(LoanCreationCommand command) {
        // Validações de negócio
        validator.validate(command);

        // Verificar se cliente existe
        customerRepository.findById(command.getCustomerId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND, "Cliente não encontrado para criação de empréstimo."));

        // Bloqueio por KYC renovável
        customerKycService.validateCustomerEligibleForLoan(command.getCustomerId(), Instant.now());

        // Montar dados de criação
        LoanCreationData creationData = LoanCreationData.builder()
                .customerId(command.getCustomerId())
                .valorSolicitado(command.getValorSolicitado())
                .taxaJuros(command.getTaxaJuros())
                .periodoPagamento(command.getPeriodoPagamento())
                .dataPrevistaDevolucao(command.getDataPrevistaDevolucao())
                .build();

        // Criar Loan usando factory
        Instant now = Instant.now();
        Loan loan = loanFactory.create(creationData, loanPolicy, now);

        // Salvar empréstimo
        Loan savedLoan = loanRepository.save(loan);

        // Retornar resposta
        return responseAssembler.toResponse(savedLoan);
    }
}

