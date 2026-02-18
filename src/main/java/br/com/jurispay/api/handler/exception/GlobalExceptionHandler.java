package br.com.jurispay.api.handler.exception;

import br.com.jurispay.api.dto.common.ErrorResponse;
import br.com.jurispay.domain.exception.common.BusinessException;
import br.com.jurispay.domain.exception.common.NotFoundException;
import br.com.jurispay.domain.exception.common.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

import java.util.stream.Collectors;

/**
 * Handler global para tratamento de exceções da API.
 * Garante que mensagens de erro sejam neutras e não exponham dados sensíveis.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final boolean includeDebugDetails;

    public GlobalExceptionHandler(
            @Value("${app.errors.include-debug-details:false}") boolean includeDebugDetails) {
        this.includeDebugDetails = includeDebugDetails;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {
        String traceId = ErrorResponseFactory.newTraceId();
        HttpStatus status = BusinessExceptionStatusResolver.resolveHttpStatus(ex.getCode());

        log.error("BusinessException traceId={}, path={}, status={}, code={}, message={}",
                traceId, request.getRequestURI(), status.value(), ex.getCode(), ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponseFactory.build(
                status,
                BusinessExceptionStatusResolver.resolveErrorTitle(status),
                ex.getCode(),
                ex.getMessage(),
                request,
                traceId
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            NotFoundException ex,
            HttpServletRequest request) {
        return handleBusinessException(ex, request);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex,
            HttpServletRequest request) {
        return handleBusinessException(ex, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            BadCredentialsException ex,
            HttpServletRequest request) {
        // Não logar detalhes sensíveis (LGPD)
        String traceId = ErrorResponseFactory.newTraceId();
        log.warn("BadCredentialsException traceId={}, path={}", traceId, request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponseFactory.build(
                HttpStatus.UNAUTHORIZED,
                "Credenciais inválidas",
                null,
                "Credenciais inválidas.",
                request,
                traceId
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request) {
        String traceId = ErrorResponseFactory.newTraceId();
        log.warn("AccessDeniedException traceId={}, path={}", traceId, request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponseFactory.build(
                HttpStatus.FORBIDDEN,
                "Acesso negado",
                null,
                "Acesso negado.",
                request,
                traceId
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        String traceId = ErrorResponseFactory.newTraceId();
        log.warn("HttpMessageNotReadableException traceId={}, path={}, message={}",
                traceId, request.getRequestURI(), ex.getMessage());

        ErrorResponse errorResponse = ErrorResponseFactory.buildWithDebug(
                HttpStatus.BAD_REQUEST,
                "Requisição inválida",
                null,
                "Corpo da requisição inválido.",
                request,
                traceId,
                includeDebugDetails,
                ex
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request) {
        String traceId = ErrorResponseFactory.newTraceId();
        String message = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));

        log.warn("ConstraintViolationException traceId={}, path={}, errors={}",
                traceId, request.getRequestURI(), message);

        ErrorResponse errorResponse = ErrorResponseFactory.build(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Erro de validação",
                null,
                message,
                request,
                traceId
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        String traceId = ErrorResponseFactory.newTraceId();
        log.error("DataIntegrityViolationException traceId={}, path={}, message={}",
                traceId, request.getRequestURI(), ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponseFactory.buildWithDebug(
                HttpStatus.CONFLICT,
                "Conflito",
                null,
                "Operação não pôde ser concluída devido a conflito de dados.",
                request,
                traceId,
                includeDebugDetails,
                ex
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        String traceId = ErrorResponseFactory.newTraceId();
        log.error("MethodArgumentNotValidException traceId={}, path={}, errors={}",
                traceId, request.getRequestURI(), errorMessage, ex);

        ErrorResponse errorResponse = ErrorResponseFactory.build(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Erro de validação",
                null,
                errorMessage,
                request,
                traceId
        );

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {
        String traceId = ErrorResponseFactory.newTraceId();
        log.error("Unhandled exception traceId={}, path={}, exceptionClass={}, message={}",
                traceId, request.getRequestURI(), ex.getClass().getName(), ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponseFactory.buildWithDebug(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor",
                null,
                "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                request,
                traceId,
                includeDebugDetails,
                ex
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

