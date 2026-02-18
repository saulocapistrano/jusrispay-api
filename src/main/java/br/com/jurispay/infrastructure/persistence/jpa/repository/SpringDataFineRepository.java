package br.com.jurispay.infrastructure.persistence.jpa.repository;

import br.com.jurispay.infrastructure.persistence.jpa.entity.FineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataFineRepository extends JpaRepository<FineEntity, Long> {

    List<FineEntity> findByAtivoTrue();

    boolean existsByNomeIgnoreCase(String nome);
}
