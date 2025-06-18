package com.quest.bank_card.exception.handler;

import com.quest.bank_card.dto.ErrorResponseDto;
import com.quest.bank_card.exception.BaseException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Centralized exception handling for all REST API endpoints.
 * <p>
 * Provides consistent error response format across the application using
 * {@link ErrorResponseDto}.
 * All unhandled exceptions are caught by the fallback handler to prevent
 * exposure of internal system details.
 */
@ControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponseDto> handleBaseException(BaseException ex) {
        ErrorResponseDto error = new ErrorResponseDto(UUID.randomUUID(),
                ex.getErrorCode(),
                ex.getMessage(),
                LocalDateTime.now(),
                ex.getDetails(),
                ex.getHttpStatus().value());
        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

        /**
     * Handles Spring validation errors by extracting field-level error details.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleArgumentValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(e -> {
            String fieldName = ((FieldError) e).getField();
            Object errorMessage =  e.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponseDto error = new ErrorResponseDto(UUID.randomUUID(),
                "ARGUMENT_VALIDATION_ERROR",
                ex.getMessage(),
                LocalDateTime.now(),
                errors,
                HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        ErrorResponseDto error = new ErrorResponseDto(UUID.randomUUID(),
                "HTTP_ERROR",
                ex.getMessage(),
                LocalDateTime.now(),
                null,
                HttpStatus.METHOD_NOT_ALLOWED.value());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        ErrorResponseDto error = new ErrorResponseDto(UUID.randomUUID(),
                "HTTP_ERROR",
                ex.getMessage(),
                LocalDateTime.now(),
                null,
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponseDto error = new ErrorResponseDto(UUID.randomUUID(),
                "ACCESS_DENIED_ERROR",
                ex.getMessage(),
                LocalDateTime.now(),
                null,
                HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    /**
     * Fallback handler for all unhandled exceptions to prevent exposure
     * of internal system details while maintaining consistent error format.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneral(Exception ex) {
        ErrorResponseDto error = new ErrorResponseDto(UUID.randomUUID(),
                "ERROR",
                ex.getMessage(),
                LocalDateTime.now(),
                null,
                HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
