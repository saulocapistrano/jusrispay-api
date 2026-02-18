package br.com.jurispay.application.fine.usecase.impl;

import br.com.jurispay.api.dto.fine.FineRequest;
import br.com.jurispay.api.dto.fine.FineResponse;
import br.com.jurispay.application.fine.assembler.FineResponseAssembler;
import br.com.jurispay.application.fine.usecase.UpdateFineUseCase;
import br.com.jurispay.application.fine.validator.FineRequestValidator;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.fine.exception.FineDomainException;
import br.com.jurispay.domain.fine.model.Fine;
import br.com.jurispay.domain.fine.repository.FineRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UpdateFineUseCaseImpl implements UpdateFineUseCase {

    private final FineRepository repository;
    private final FineResponseAssembler assembler;
    private final FineRequestValidator validator;

    public UpdateFineUseCaseImpl(
            FineRepository repository,
            FineResponseAssembler assembler,
            FineRequestValidator validator) {
        this.repository = repository;
        this.assembler = assembler;
        this.validator = validator;
    }

    @Override
    public FineResponse update(Long id, FineRequest request) {
        if (id == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "id é obrigatório.");
        }
        validator.validate(request);

        Fine existing = repository.findById(id)
                .orElseThrow(() -> FineDomainException.of("FINE_NOT_FOUND", "Multa não encontrada."));

        Instant now = Instant.now();
        Fine updated = Fine.builder()
                .id(existing.getId())
                .nome(request.getNome().trim())
                .valor(request.getValor())
                .ativo(Boolean.TRUE.equals(request.getAtivo()))
                .dataCriacao(existing.getDataCriacao())
                .dataAtualizacao(now)
                .build();

        Fine saved = repository.save(updated);
        return assembler.toResponse(saved);
    }
}
