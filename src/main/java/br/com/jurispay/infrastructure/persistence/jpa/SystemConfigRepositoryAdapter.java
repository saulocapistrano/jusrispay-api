package br.com.jurispay.infrastructure.persistence.jpa;

import br.com.jurispay.domain.systemconfig.model.SystemConfig;
import br.com.jurispay.domain.systemconfig.repository.SystemConfigRepository;
import br.com.jurispay.infrastructure.persistence.jpa.repository.SpringDataSystemConfigRepository;
import br.com.jurispay.infrastructure.persistence.mapper.SystemConfigEntityMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SystemConfigRepositoryAdapter implements SystemConfigRepository {

    private final SpringDataSystemConfigRepository repository;
    private final SystemConfigEntityMapper mapper;

    public SystemConfigRepositoryAdapter(SpringDataSystemConfigRepository repository, SystemConfigEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<SystemConfig> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public SystemConfig save(SystemConfig config) {
        var saved = repository.save(mapper.toEntity(config));
        return mapper.toDomain(saved);
    }
}
