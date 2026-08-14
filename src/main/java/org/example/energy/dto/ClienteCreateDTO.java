package org.example.energy.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ClienteCreateDTO(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        @Size(max = 150, message = "El email no puede superar los 150 caracteres")
        String email,

        @NotBlank(message = "El tipo de cliente es obligatorio")
        @Pattern(
                regexp = "^(?i)(residencial|empresa|industrial)$",
                message = "El tipo debe ser 'residencial', 'empresa' o 'industrial'"
        )
        String tipo,

        @Size(max = 50, message = "La ciudad no puede superar los 50 caracteres")
        String ciudad,

        @NotNull(message = "La fecha de alta es obligatoria")
        @PastOrPresent(message = "La fecha de alta no puede ser futura")
        LocalDate fechaAlta,

        @Pattern(
                regexp = "^(?i)(Nuevo|Regular|Premium|VIP)$",
                message = "El segmento debe ser 'Nuevo', 'Regular', 'Premium' o 'VIP'"
        )
        String segmento
) {
}
