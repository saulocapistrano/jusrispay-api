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
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.policy.LoanPolicy;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import br.com.jurispay.domain.loantype.repository.LoanTypeRepository;
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
    private final LoanTypeRepository loanTypeRepository;

    public CreateLoanUseCaseImpl(
            LoanRepository loanRepository,
            CustomerRepository customerRepository,
            LoanResponseAssembler responseAssembler,
            LoanCreationCommandValidator validator,
            LoanFactory loanFactory,
            LoanPolicy loanPolicy,
            CustomerKycService customerKycService,
            LoanTypeRepository loanTypeRepository) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.responseAssembler = responseAssembler;
        this.validator = validator;
        this.loanFactory = loanFactory;
        this.loanPolicy = loanPolicy;
        this.customerKycService = customerKycService;
        this.loanTypeRepository = loanTypeRepository;
    }

    @Override
    public LoanResponse create(LoanCreationCommand command) {
        // Validações de negócio
        validator.validate(command);

        // Verificar se cliente existe
        customerRepository.findById(command.getCustomerId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.CUSTOMER_NOT_FOUND, "Cliente não encontrado para criação de empréstimo."));

        loanTypeRepository.findById(command.getLoanTypeId())
                .filter(t -> Boolean.TRUE.equals(t.getAtivo()))
                .orElseThrow(() -> new br.com.jurispay.domain.exception.common.ValidationException(
                        ErrorCode.BUSINESS_RULE_VIOLATION,
                        "Tipo de empréstimo não encontrado ou inativo."));

        // Bloqueio por KYC renovável
        boolean allowKycIncomplete = Boolean.TRUE.equals(command.getAllowKycIncomplete());
        try {
            customerKycService.validateCustomerEligibleForLoan(command.getCustomerId(), Instant.now());
        } catch (br.com.jurispay.domain.exception.common.ValidationException ex) {
            if (!allowKycIncomplete) {
                throw ex;
            }
        }

        // Montar dados de criação
        LoanCreationData creationData = LoanCreationData.builder()
                .customerId(command.getCustomerId())
                .loanTypeId(command.getLoanTypeId())
                .valorSolicitado(command.getValorSolicitado())
                .taxaJuros(command.getTaxaJuros())
                .periodoPagamento(command.getPeriodoPagamento())
                .dataPrevistaDevolucao(command.getDataPrevistaDevolucao())
                .build();

        // Criar Loan usando factory
        Instant now = Instant.now();
        Loan loan = loanFactory.create(creationData, loanPolicy, now);

        if (allowKycIncomplete) {
            loan = Loan.builder()
                    .id(loan.getId())
                    .customerId(loan.getCustomerId())
                    .valorSolicitado(loan.getValorSolicitado())
                    .valorDevolucaoPrevista(loan.getValorDevolucaoPrevista())
                    .taxaJuros(loan.getTaxaJuros())
                    .multaDiaria(loan.getMultaDiaria())
                    .periodoPagamento(loan.getPeriodoPagamento())
                    .quantidadeParcelas(loan.getQuantidadeParcelas())
                    .valorParcela(loan.getValorParcela())
                    .dataLiberacao(loan.getDataLiberacao())
                    .dataPrevistaDevolucao(loan.getDataPrevistaDevolucao())
                    .dataCriacao(loan.getDataCriacao())
                    .dataAtualizacao(now)
                    .status(LoanStatus.PENDING_DOCUMENTS)
                    .build();
        }

        // Salvar empréstimo
        Loan savedLoan = loanRepository.save(loan);

        // Retornar resposta
        return responseAssembler.toResponse(savedLoan);
    }
}

