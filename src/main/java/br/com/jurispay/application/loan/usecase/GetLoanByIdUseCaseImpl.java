package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.application.loan.dto.LoanResponse;
import br.com.jurispay.application.loan.assembler.LoanResponseAssembler;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.loan.model.Loan;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;

/**
 * Implementação do caso de uso de busca de empréstimo por ID.
 */
@Service
public class GetLoanByIdUseCaseImpl implements GetLoanByIdUseCase {

    private final LoanRepository loanRepository;
    private final LoanResponseAssembler responseAssembler;

    public GetLoanByIdUseCaseImpl(
            LoanRepository loanRepository,
            LoanResponseAssembler responseAssembler) {
        this.loanRepository = loanRepository;
        this.responseAssembler = responseAssembler;
    }

    @Override
    public LoanResponse getById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empréstimo não encontrado."));

        return responseAssembler.toResponse(loan);
    }
}

