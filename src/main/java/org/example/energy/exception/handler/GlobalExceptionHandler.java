package org.example.energy.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.error.ErrorResponseDTO;
import org.example.energy.dto.error.FieldErrorDTO;
import org.example.energy.exception.code.ErrorCode;
import org.example.energy.exception.type.BusinessRuleException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.example.energy.exception.type.MethodArgumentTypeMismatchException;
import org.example.energy.exception.type.ResourceNotFoundException;
import org.example.energy.mapper.ErrorMapper;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorMapper errorMapper;

    // Recurso no encontrado (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {

        log.warn("Recurso no encontrado. path={}, message={}",
                request.getRequestURI(),
                ex.getMessage()
        );
        ErrorCode ec = ErrorCode.RESOURCE_NOT_FOUND;
        ErrorResponseDTO errorDTO = errorMapper.toErrorResponseDTO(ec, ex.getMessage(), request.getRequestURI());

        return ResponseEntity.status(ec.getHttpStatus()).body(errorDTO);
    }

    // Error validacion
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        ErrorCode ec = ErrorCode.INVALID_INPUT;
        List<FieldErrorDTO> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> new FieldErrorDTO(f.getField(), f.getDefaultMessage()))
                .toList();

        ErrorResponseDTO errorDTO = errorMapper.toErrorResponseDTO(
                ec,
                "Error de validación en los datos de entrada",
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity.status(ec.getHttpStatus()).body(errorDTO);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        log.warn("Tipo de parámetro inválido. path={}, message={}",
                request.getRequestURI(),
                ex.getMessage()
        );
        ErrorCode ec = ErrorCode.INVALID_INPUT;

        ErrorResponseDTO errorResponseDTO = errorMapper.toErrorResponseDTO(
                ec,
                "El parámetro de la ruta no coincide con el tipo esperado",
                request.getRequestURI()
        );

        return ResponseEntity.status(ec.getHttpStatus()).body(errorResponseDTO);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        log.warn(
                "Body de la petición no legible. path={}, detalle={}",
                request.getRequestURI(),
                ex.getMessage()
        );
        ErrorCode ec = ErrorCode.MALFORMED_JSON;

        ErrorResponseDTO errorResponseDTO = errorMapper.toErrorResponseDTO(
                ec,
                ec.getDefaultMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(ec.getHttpStatus()).body(errorResponseDTO);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessRuleException(
            BusinessRuleException ex,
            HttpServletRequest request
    ) {
        ErrorCode ec = ex.getErrorCode();

        log.warn(
                "Regla de negocio incumplida. path={}, errorCode={}, message={}",
                request.getRequestURI(),
                ec.name(),
                ex.getMessage()
        );

        ErrorResponseDTO errorDTO = errorMapper.toErrorResponseDTO(
                ec,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(ec.getHttpStatus())
                .body(errorDTO);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex, HttpServletRequest request) {

        System.out.println("CLASE REAL DE LA EXCEPCIÓN: " + ex.getClass().getName());
        if (ex.getCause() != null) {
            System.out.println("CAUSA DE LA EXCEPCIÓN: " + ex.getCause().getClass().getName());
        }

        log.error("Error inesperado. path={}", request.getRequestURI(), ex);
        ErrorCode ec = ErrorCode.INTERNAL_ERROR;
        return ResponseEntity.status(ec.getHttpStatus())
                .body(errorMapper.toErrorResponseDTO(
                        ec,
                        ex.getMessage() != null ? ex.getMessage() : ec.getDefaultMessage(),
                        request.getRequestURI()));
    }
}