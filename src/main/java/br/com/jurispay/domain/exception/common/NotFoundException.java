package br.com.jurispay.domain.exception.common;

/**
 * Exceção lançada quando um recurso não é encontrado.
 * Exemplos: cliente, empréstimo, pagamento não encontrados.
 */
public class NotFoundException extends BusinessException {

    private static final String DEFAULT_CODE = "NOT_FOUND";

    public NotFoundException(String message) {
        super(DEFAULT_CODE, message);
    }

    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode.getCode(), message);
    }

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getDefaultMessage());
    }
}

