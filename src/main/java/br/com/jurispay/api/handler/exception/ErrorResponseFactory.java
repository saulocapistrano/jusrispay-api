package br.com.jurispay.api.handler.exception;

import br.com.jurispay.api.dto.common.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.UUID;

final class ErrorResponseFactory {

    private ErrorResponseFactory() {
    }

    static String newTraceId() {
        return UUID.randomUUID().toString();
    }

    static ErrorResponse build(
            HttpStatus status,
            String error,
            String code,
            String message,
            HttpServletRequest request,
            String traceId) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(error)
                .code(code)
                .message(message)
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();
    }

    static ErrorResponse buildWithDebug(
            HttpStatus status,
            String error,
            String code,
            String message,
            HttpServletRequest request,
            String traceId,
            boolean includeDebugDetails,
            Exception ex) {
        ErrorResponse.ErrorResponseBuilder builder = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(error)
                .code(code)
                .message(message)
                .path(request.getRequestURI())
                .traceId(traceId);

        if (includeDebugDetails && ex != null) {
            builder.exceptionClass(ex.getClass().getName()).debugMessage(ex.getMessage());
        }

        return builder.build();
    }
}
