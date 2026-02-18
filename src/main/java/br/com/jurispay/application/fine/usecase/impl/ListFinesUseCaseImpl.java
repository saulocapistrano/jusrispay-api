package br.com.jurispay.application.fine.usecase.impl;

import br.com.jurispay.api.dto.fine.FineResponse;
import br.com.jurispay.application.fine.assembler.FineResponseAssembler;
import br.com.jurispay.application.fine.usecase.ListFinesUseCase;
import br.com.jurispay.domain.fine.repository.FineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListFinesUseCaseImpl implements ListFinesUseCase {

    private final FineRepository repository;
    private final FineResponseAssembler assembler;

    public ListFinesUseCaseImpl(FineRepository repository, FineResponseAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @Override
    public List<FineResponse> listAll() {
        return repository.findAll().stream().map(assembler::toResponse).toList();
    }
}
