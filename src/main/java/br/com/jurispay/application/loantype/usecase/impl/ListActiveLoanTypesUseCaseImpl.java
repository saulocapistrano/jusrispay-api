package br.com.jurispay.application.loantype.usecase.impl;

import br.com.jurispay.api.dto.loantype.LoanTypeResponse;
import br.com.jurispay.application.loantype.assembler.LoanTypeResponseAssembler;
import br.com.jurispay.application.loantype.usecase.ListActiveLoanTypesUseCase;
import br.com.jurispay.domain.loantype.repository.LoanTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListActiveLoanTypesUseCaseImpl implements ListActiveLoanTypesUseCase {

    private final LoanTypeRepository repository;
    private final LoanTypeResponseAssembler assembler;

    public ListActiveLoanTypesUseCaseImpl(LoanTypeRepository repository, LoanTypeResponseAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @Override
    public List<LoanTypeResponse> listActive() {
        return repository.findAllActive().stream().map(assembler::toResponse).toList();
    }
}
