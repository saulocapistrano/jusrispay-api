package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.application.loan.dto.LoanCreationCommand;
import br.com.jurispay.application.loan.dto.LoanResponse;
import br.com.jurispay.application.loan.mapper.LoanApplicationMapper;
import br.com.jurispay.domain.common.exception.NotFoundException;
import br.com.jurispay.domain.common.exception.ValidationException;
import br.com.jurispay.domain.creditanalysis.model.CreditAnalysisStatus;
import br.com.jurispay.domain.creditanalysis.repository.CreditAnalysisRepository;
import br.com.jurispay.domain.customer.repository.CustomerRepository;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.model.LoanStatus;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Implementação do caso de uso de criação de empréstimo.
 */
@Service
public class CreateLoanUseCaseImpl implements CreateLoanUseCase {

    private static final BigDecimal TAXA_JUROS_PADRAO = new BigDecimal("0.30");
    private static final BigDecimal MULTA_DIARIA_PADRAO = new BigDecimal("20.00");

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final CreditAnalysisRepository creditAnalysisRepository;
    private final LoanApplicationMapper mapper;

    public CreateLoanUseCaseImpl(
            LoanRepository loanRepository,
            CustomerRepository customerRepository,
            CreditAnalysisRepository creditAnalysisRepository,
            LoanApplicationMapper mapper) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
        this.creditAnalysisRepository = creditAnalysisRepository;
        this.mapper = mapper;
    }

    @Override
    public LoanResponse create(LoanCreationCommand command) {
        // Validações de negócio
        validateCommand(command);

        // Verificar se cliente existe
        customerRepository.findById(command.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado para criação de empréstimo."));

        // Verificar se cliente está aprovado na análise de crédito
        creditAnalysisRepository.findByCustomerId(command.getCustomerId())
                .filter(analysis -> analysis.getStatus() == CreditAnalysisStatus.APPROVED)
                .orElseThrow(() -> new ValidationException("Cliente não aprovado na análise de crédito."));

        // Criar Loan base a partir do comando
        Loan loan = mapper.toDomain(command);

        // Definir valores padrão e calculados
        Instant now = Instant.now();
        BigDecimal valorDevolucaoPrevista = command.getValorSolicitado()
                .multiply(BigDecimal.ONE.add(TAXA_JUROS_PADRAO));

        // Construir Loan completo usando builder
        Loan loanCompleto = Loan.builder()
                .customerId(loan.getCustomerId())
                .valorSolicitado(loan.getValorSolicitado())
                .dataPrevistaDevolucao(loan.getDataPrevistaDevolucao())
                .taxaJuros(TAXA_JUROS_PADRAO)
                .multaDiaria(MULTA_DIARIA_PADRAO)
                .valorDevolucaoPrevista(valorDevolucaoPrevista)
                .dataLiberacao(now)
                .dataCriacao(now)
                .dataAtualizacao(now)
                .status(LoanStatus.OPEN)
                .build();

        // Salvar empréstimo
        Loan savedLoan = loanRepository.save(loanCompleto);

        // Retornar resposta
        return mapper.toResponse(savedLoan);
    }

    private void validateCommand(LoanCreationCommand command) {
        if (command.getValorSolicitado() == null || command.getValorSolicitado().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Valor solicitado deve ser maior que zero.");
        }

        if (command.getDataPrevistaDevolucao() == null || command.getDataPrevistaDevolucao().isBefore(Instant.now())) {
            throw new ValidationException("Data prevista de devolução deve ser futura.");
        }
    }
}

