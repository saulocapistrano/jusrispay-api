package br.com.jurispay.application.loantype.usecase.impl;

import br.com.jurispay.api.dto.loantype.LoanTypeResponse;
import br.com.jurispay.application.loantype.assembler.LoanTypeResponseAssembler;
import br.com.jurispay.application.loantype.usecase.ListLoanTypesUseCase;
import br.com.jurispay.domain.loantype.repository.LoanTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListLoanTypesUseCaseImpl implements ListLoanTypesUseCase {

    private final LoanTypeRepository repository;
    private final LoanTypeResponseAssembler assembler;

    public ListLoanTypesUseCaseImpl(LoanTypeRepository repository, LoanTypeResponseAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @Override
    public List<LoanTypeResponse> listAll() {
        return repository.findAll().stream().map(assembler::toResponse).toList();
    }
}
