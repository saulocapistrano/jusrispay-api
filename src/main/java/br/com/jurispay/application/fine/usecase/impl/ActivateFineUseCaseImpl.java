package br.com.jurispay.application.fine.usecase.impl;

import br.com.jurispay.application.fine.usecase.ActivateFineUseCase;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.ValidationException;
import br.com.jurispay.domain.fine.exception.FineDomainException;
import br.com.jurispay.domain.fine.model.Fine;
import br.com.jurispay.domain.fine.repository.FineRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ActivateFineUseCaseImpl implements ActivateFineUseCase {

    private final FineRepository repository;

    public ActivateFineUseCaseImpl(FineRepository repository) {
        this.repository = repository;
    }

    @Override
    public void activate(Long id) {
        if (id == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "id é obrigatório.");
        }

        Fine existing = repository.findById(id)
                .orElseThrow(() -> FineDomainException.of("FINE_NOT_FOUND", "Multa não encontrada."));

        Fine updated = Fine.builder()
                .id(existing.getId())
                .nome(existing.getNome())
                .valor(existing.getValor())
                .ativo(true)
                .dataCriacao(existing.getDataCriacao())
                .dataAtualizacao(Instant.now())
                .build();

        repository.save(updated);
    }
}
