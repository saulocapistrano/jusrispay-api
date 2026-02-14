package br.com.jurispay.application.common.validator;

import br.com.jurispay.domain.exception.common.ValidationException;

/**
 * Interface genérica para validação de comandos.
 * Permite extrair lógica de validação dos use cases para classes dedicadas.
 *
 * @param <T> tipo do comando a ser validado
 */
public interface CommandValidator<T> {

    /**
     * Valida o comando e lança ValidationException se houver erros.
     *
     * @param command comando a ser validado
     * @throws ValidationException se o comando for inválido
     */
    void validate(T command);
}

