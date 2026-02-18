package br.com.jurispay.application.fine.usecase.impl;

import br.com.jurispay.api.dto.fine.FineResponse;
import br.com.jurispay.application.fine.assembler.FineResponseAssembler;
import br.com.jurispay.application.fine.usecase.ListActiveFinesUseCase;
import br.com.jurispay.domain.fine.repository.FineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListActiveFinesUseCaseImpl implements ListActiveFinesUseCase {

    private final FineRepository repository;
    private final FineResponseAssembler assembler;

    public ListActiveFinesUseCaseImpl(FineRepository repository, FineResponseAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @Override
    public List<FineResponse> listActive() {
        return repository.findAllActive().stream().map(assembler::toResponse).toList();
    }
}
