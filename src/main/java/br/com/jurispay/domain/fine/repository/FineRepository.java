package br.com.jurispay.domain.fine.repository;

import br.com.jurispay.domain.fine.model.Fine;

import java.util.List;
import java.util.Optional;

public interface FineRepository {

    Fine save(Fine fine);

    Optional<Fine> findById(Long id);

    List<Fine> findAll();

    List<Fine> findAllActive();

    boolean existsByNomeIgnoreCase(String nome);
}
