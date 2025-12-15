package br.com.jurispay.infrastructure.config.security.strategy;

import br.com.jurispay.infrastructure.config.security.model.DevUserDefinition;

import java.util.Optional;

/**
 * Interface Strategy para fonte de usuários.
 * Permite diferentes implementações (in-memory, database, etc.) sem alterar o código cliente.
 */
public interface UserSource {

    /**
     * Busca um usuário pelo username.
     *
     * @param username nome de usuário a buscar
     * @return Optional contendo o usuário se encontrado, vazio caso contrário
     */
    Optional<DevUserDefinition> findByUsername(String username);
}

