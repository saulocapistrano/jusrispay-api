package br.com.jurispay.application.fine.usecase.impl;

import br.com.jurispay.api.dto.fine.FineRequest;
import br.com.jurispay.api.dto.fine.FineResponse;
import br.com.jurispay.application.fine.assembler.FineResponseAssembler;
import br.com.jurispay.application.fine.usecase.CreateFineUseCase;
import br.com.jurispay.application.fine.validator.FineRequestValidator;
import br.com.jurispay.domain.fine.exception.FineDomainException;
import br.com.jurispay.domain.fine.model.Fine;
import br.com.jurispay.domain.fine.repository.FineRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CreateFineUseCaseImpl implements CreateFineUseCase {

    private final FineRepository repository;
    private final FineResponseAssembler assembler;
    private final FineRequestValidator validator;

    public CreateFineUseCaseImpl(
            FineRepository repository,
            FineResponseAssembler assembler,
            FineRequestValidator validator) {
        this.repository = repository;
        this.assembler = assembler;
        this.validator = validator;
    }

    @Override
    public FineResponse create(FineRequest request) {
        validator.validate(request);

        if (repository.existsByNomeIgnoreCase(request.getNome())) {
            throw FineDomainException.of("FINE_DUPLICATE_NAME", "Já existe uma multa com este nome.");
        }

        Instant now = Instant.now();
        Fine fine = Fine.builder()
                .id(null)
                .nome(request.getNome().trim())
                .valor(request.getValor())
                .ativo(Boolean.TRUE.equals(request.getAtivo()))
                .dataCriacao(now)
                .dataAtualizacao(now)
                .build();

        Fine saved = repository.save(fine);
        return assembler.toResponse(saved);
    }
}
