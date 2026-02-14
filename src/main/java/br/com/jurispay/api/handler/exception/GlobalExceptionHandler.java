package br.com.jurispay.api.handler.exception;

import br.com.jurispay.api.dto.common.ErrorResponse;
import br.com.jurispay.domain.exception.common.BusinessException;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.exception.common.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Handler global para tratamento de exceções da API.
 * Garante que mensagens de erro sejam neutras e não exponham dados sensíveis.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {
        log.error("BusinessException on path={}, code={}, message={}", 
                request.getRequestURI(), ex.getCode(), ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Erro de negócio")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .traceId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            NotFoundException ex,
            HttpServletRequest request) {
        log.error("NotFoundException on path={}, message={}", 
                request.getRequestURI(), ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Recurso não encontrado")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .traceId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex,
            HttpServletRequest request) {
        log.error("ValidationException on path={}, message={}", 
                request.getRequestURI(), ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error("Erro de validação")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .traceId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            BadCredentialsException ex,
            HttpServletRequest request) {
        // Não logar detalhes sensíveis (LGPD)
        log.warn("BadCredentialsException on path={}", request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Credenciais inválidas")
                .message("Credenciais inválidas.")
                .path(request.getRequestURI())
                .traceId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.error("MethodArgumentNotValidException on path={}, errors={}", 
                request.getRequestURI(), errorMessage, ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error("Erro de validação")
                .message(errorMessage)
                .path(request.getRequestURI())
                .traceId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        log.error("Unhandled exception on path={}, exceptionClass={}, message={}", 
                request.getRequestURI(), ex.getClass().getName(), ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Erro interno do servidor")
                .message("Ocorreu um erro inesperado. Tente novamente mais tarde.")
                .path(request.getRequestURI())
                .traceId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

