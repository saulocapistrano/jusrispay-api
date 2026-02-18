package br.com.jurispay.domain.receivable.exception;

import br.com.jurispay.domain.exception.common.BusinessException;

public class ReceivableDomainException extends BusinessException {

    public ReceivableDomainException(String code, String message) {
        super(code, message);
    }

    public static ReceivableDomainException of(String code, String message) {
        return new ReceivableDomainException(code, message);
    }
}
