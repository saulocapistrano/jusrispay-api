package br.com.jurispay.domain.systemconfig.repository;

import br.com.jurispay.domain.systemconfig.model.SystemConfig;

import java.util.Optional;

public interface SystemConfigRepository {

    Optional<SystemConfig> findById(Long id);

    SystemConfig save(SystemConfig config);
}
