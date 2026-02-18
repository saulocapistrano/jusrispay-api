package br.com.jurispay.application.loan.usecase;

import br.com.jurispay.application.loan.dto.LoanResponse;
import br.com.jurispay.application.loan.assembler.LoanResponseAssembler;
import br.com.jurispay.domain.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do caso de uso de listagem de empréstimos.
 */
@Service
public class ListLoansUseCaseImpl implements ListLoansUseCase {

    private final LoanRepository loanRepository;
    private final LoanResponseAssembler responseAssembler;

    public ListLoansUseCaseImpl(
            LoanRepository loanRepository,
            LoanResponseAssembler responseAssembler) {
        this.loanRepository = loanRepository;
        this.responseAssembler = responseAssembler;
    }

    @Override
    public List<LoanResponse> listAll() {
        return loanRepository.findAll().stream()
                .map(responseAssembler::toResponse)
                .collect(Collectors.toList());
    }
}

