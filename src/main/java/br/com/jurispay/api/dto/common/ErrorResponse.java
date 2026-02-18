package br.com.jurispay.api.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO para respostas de erro da API.
 * Não inclui dados sensíveis como CPF, chave PIX, etc.
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private Instant timestamp;
    private int status;
    private String error;
    private String code;
    private String message;
    private String path;
    private String traceId;

    private String exceptionClass;
    private String debugMessage;
}

