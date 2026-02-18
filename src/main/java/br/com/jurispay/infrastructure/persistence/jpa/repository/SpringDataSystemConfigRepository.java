package br.com.jurispay.infrastructure.persistence.jpa.repository;

import br.com.jurispay.infrastructure.persistence.jpa.entity.SystemConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSystemConfigRepository extends JpaRepository<SystemConfigEntity, Long> {
}
