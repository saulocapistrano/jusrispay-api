package br.com.jurispay.domain.loantype.exception;

import br.com.jurispay.domain.exception.common.BusinessException;

public class LoanTypeDomainException extends BusinessException {

    public LoanTypeDomainException(String code, String message) {
        super(code, message);
    }

    public static LoanTypeDomainException of(String code, String message) {
        return new LoanTypeDomainException(code, message);
    }
}
