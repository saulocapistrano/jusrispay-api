package br.com.jurispay.api.handler.exception;

import br.com.jurispay.domain.exception.common.ErrorCode;
import org.springframework.http.HttpStatus;

final class BusinessExceptionStatusResolver {

    private BusinessExceptionStatusResolver() {
    }

    static HttpStatus resolveHttpStatus(String code) {
        if (code == null || code.isBlank()) {
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }

        if (ErrorCode.UNAUTHORIZED.getCode().equals(code)
            || ErrorCode.INVALID_CREDENTIALS.getCode().equals(code)
            || "UNAUTHORIZED".equals(code)
            || "INVALID_CREDENTIALS".equals(code)) {
            return HttpStatus.UNAUTHORIZED;
        }

        if (ErrorCode.FORBIDDEN.getCode().equals(code) || "FORBIDDEN".equals(code)) {
            return HttpStatus.FORBIDDEN;
        }

        if (code.endsWith("_NOT_FOUND") || ErrorCode.NOT_FOUND.getCode().equals(code)) {
            return HttpStatus.NOT_FOUND;
        }

        // Default: regra de negócio/validação semântico (domínio)
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }

    static String resolveErrorTitle(HttpStatus status) {
        if (status == HttpStatus.NOT_FOUND) {
            return "Recurso não encontrado";
        }
        if (status == HttpStatus.UNAUTHORIZED) {
            return "Não autorizado";
        }
        if (status == HttpStatus.FORBIDDEN) {
            return "Acesso negado";
        }
        if (status == HttpStatus.CONFLICT) {
            return "Conflito";
        }
        if (status == HttpStatus.BAD_REQUEST) {
            return "Requisição inválida";
        }
        return "Violação de regra de negócio";
    }
}
