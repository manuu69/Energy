package org.example.energy.dto.zona;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ZonaCreateDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,

        @NotBlank(message = "El nivel es obligatorio")
        Integer nivel,

        @NotBlank(message = "El tipo de cliente es obligatorio")
        @Size(max = 200, message = "La descripcion no puede superar los 200 caracteres")
        String descripcion
) {
}
