package br.com.jurispay.domain.fine.exception;

import br.com.jurispay.domain.exception.common.BusinessException;

public class FineDomainException extends BusinessException {

    public FineDomainException(String code, String message) {
        super(code, message);
    }

    public static FineDomainException of(String code, String message) {
        return new FineDomainException(code, message);
    }
}
