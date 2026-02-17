package br.com.jurispay.domain.payment.exception;

import br.com.jurispay.domain.exception.common.BusinessException;

public class PaymentDomainException extends BusinessException {

    public PaymentDomainException(String code, String message) {
        super(code, message);
    }

    public static PaymentDomainException of(String code, String message) {
        return new PaymentDomainException(code, message);
    }
}
