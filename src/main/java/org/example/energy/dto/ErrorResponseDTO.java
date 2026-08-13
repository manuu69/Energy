package org.example.energy.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String errorCode,
        String error,
        String message,
        String path,
        List<FieldErrorDTO> validationErrors
) {
    // Constructor para errores sin lista de campos
    public ErrorResponseDTO(int status, String errorCode, String error, String message, String path) {
        this(LocalDateTime.now(), status, errorCode, error, message, path, null);
    }

    // Constructor para errores de validación
    public ErrorResponseDTO(int status, String errorCode, String error, String message, String path, List<FieldErrorDTO> validationErrors) {
        this(LocalDateTime.now(), status, errorCode, error, message, path, validationErrors);
    }
}
