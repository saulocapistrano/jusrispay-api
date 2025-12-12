package br.com.jurispay.application.user.domain.common.exception;

/**
 * Exceção base para erros de negócio do domínio.
 * Todas as exceções de negócio devem estender esta classe.
 */
public class BusinessException extends RuntimeException {

    private final String code;
    private final String message;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Método de fábrica estático para criar uma BusinessException.
     *
     * @param code    Código do erro
     * @param message Mensagem do erro
     * @return BusinessException instanciada
     */
    public static BusinessException of(String code, String message) {
        return new BusinessException(code, message);
    }
}

