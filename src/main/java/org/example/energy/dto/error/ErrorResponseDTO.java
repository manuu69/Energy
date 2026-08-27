package org.example.energy.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String errorCode,
        String error,
        String message,
        String path,
        List<FieldErrorDTO> validationErrors
) {
    public ErrorResponseDTO(int status, String errorCode, String error, String message, String path) {
        this(LocalDateTime.now(), status, errorCode, error, message, path, null);
    }

    public ErrorResponseDTO(int status, String errorCode, String error, String message, String path, List<FieldErrorDTO> validationErrors) {
        this(LocalDateTime.now(), status, errorCode, error, message, path, validationErrors);
    }
}
