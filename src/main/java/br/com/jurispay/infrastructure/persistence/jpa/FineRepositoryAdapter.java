package br.com.jurispay.infrastructure.persistence.jpa;

import br.com.jurispay.domain.fine.model.Fine;
import br.com.jurispay.domain.fine.repository.FineRepository;
import br.com.jurispay.infrastructure.persistence.jpa.entity.FineEntity;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataFineRepository;
import br.com.jurispay.infrastructure.persistence.mapper.FineEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FineRepositoryAdapter implements FineRepository {

    private final SpringDataFineRepository repository;
    private final FineEntityMapper mapper;

    public FineRepositoryAdapter(SpringDataFineRepository repository, FineEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Fine save(Fine fine) {
        FineEntity saved = repository.save(mapper.toEntity(fine));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Fine> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Fine> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Fine> findAllActive() {
        return repository.findByAtivoTrue().stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByNomeIgnoreCase(String nome) {
        return repository.existsByNomeIgnoreCase(nome);
    }
}
