package br.com.jurispay.application.user.domain.common.exception;

/**
 * Exceção lançada quando ocorre erro de validação de dados.
 */
public class ValidationException extends BusinessException {

    private static final String DEFAULT_CODE = "VALIDATION_ERROR";

    public ValidationException(String message) {
        super(DEFAULT_CODE, message);
    }
}

