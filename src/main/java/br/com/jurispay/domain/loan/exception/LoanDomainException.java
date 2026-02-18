package br.com.jurispay.domain.loan.exception;

import br.com.jurispay.domain.exception.common.BusinessException;

public class LoanDomainException extends BusinessException {

    public LoanDomainException(String code, String message) {
        super(code, message);
    }

    public static LoanDomainException of(String code, String message) {
        return new LoanDomainException(code, message);
    }
}
