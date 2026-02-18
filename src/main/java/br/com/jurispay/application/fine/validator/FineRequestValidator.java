package br.com.jurispay.application.fine.validator;

import br.com.jurispay.api.dto.fine.FineRequest;
import br.com.jurispay.domain.exception.common.ErrorCode;
import br.com.jurispay.domain.exception.common.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FineRequestValidator {

    public void validate(FineRequest request) {
        if (request == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "Request é obrigatório.");
        }
        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "Nome é obrigatório.");
        }
        if (request.getValor() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "Valor é obrigatório.");
        }
        if (request.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(ErrorCode.INVALID_VALUE, "Valor deve ser > 0.");
        }
        if (request.getAtivo() == null) {
            throw new ValidationException(ErrorCode.REQUIRED_FIELD, "Ativo é obrigatório.");
        }
    }
}
