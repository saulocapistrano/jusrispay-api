package br.com.jurispay.infrastructure.config.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Modelo para representar um usuário de desenvolvimento/configuração.
 * Usado para definir usuários em memória ou via propriedades.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevUserDefinition {

    /**
     * Nome de usuário (username).
     */
    private String username;

    /**
     * Senha em texto plano (será codificada pelo PasswordEncoder).
     */
    private String rawPassword;

    /**
     * Lista de roles do usuário (ex: ["ADMIN"], ["OPERATOR"], ["AUDITOR"]).
     */
    private List<String> roles;
}

