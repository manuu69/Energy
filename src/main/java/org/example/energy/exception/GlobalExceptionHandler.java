package org.example.energy.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.energy.dto.ErrorResponseDTO;
import org.example.energy.dto.FieldErrorDTO;
import org.example.energy.mapper.ErrorMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorMapper errorMapper;

    // Recurso no encontrado (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {

        ErrorCode ec = ErrorCode.RESOURCE_NOT_FOUND;
        // Usamos el mapper enviando la ex.getMessage() personalizada
        ErrorResponseDTO errorDTO = errorMapper.toErrorResponseDTO(ec, ex.getMessage(), request.getRequestURI());

        return ResponseEntity.status(ec.getHttpStatus()).body(errorDTO);
    }

    // Error de validación de campos @Valid (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        ErrorCode ec = ErrorCode.INVALID_INPUT;
        List<FieldErrorDTO> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> new FieldErrorDTO(f.getField(), f.getDefaultMessage()))
                .toList();

        // Usamos el método completo con la lista de validaciones
        ErrorResponseDTO errorDTO = errorMapper.toErrorResponseDTO(
                ec,
                "Error de validación en los datos de entrada",
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity.status(ec.getHttpStatus()).body(errorDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex, HttpServletRequest request) {

        ErrorCode ec = ErrorCode.INTERNAL_ERROR;
        return ResponseEntity.status(ec.getHttpStatus())
                .body(errorMapper.toErrorResponseDTO(
                        ec,
                        ex.getMessage() != null ? ex.getMessage() : ec.getDefaultMessage(),
                        request.getRequestURI()));
    }
}