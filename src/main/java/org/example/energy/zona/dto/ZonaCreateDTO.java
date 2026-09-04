package org.example.energy.zona.dto;

import jakarta.validation.constraints.*;

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
