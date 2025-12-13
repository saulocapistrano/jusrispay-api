package br.com.jurispay.application.auth.usecase;

import br.com.jurispay.api.dto.auth.TokenResponse;

/**
 * Caso de uso para autenticação e geração de token JWT.
 */
public interface LoginUseCase {

    /**
     * Autentica usuário e retorna token JWT.
     *
     * @param username nome de usuário
     * @param password senha
     * @return resposta com token JWT
     * @throws br.com.jurispay.domain.common.exception.ValidationException se credenciais inválidas
     */
    TokenResponse login(String username, String password);
}

