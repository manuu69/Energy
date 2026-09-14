package org.example.energy.cliente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Datos requeridos para el alta de un nuevo cliente en la plataforma")
public record ClienteCreateDTO(

        @Schema(
                description = "Nombre completo o razón social del cliente",
                example = "Carlos Pérez Gómez",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,

        @Schema(
                description = "Correo electrónico de contacto principal",
                example = "carlos.perez@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 150
        )
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        @Size(max = 150, message = "El email no puede superar los 150 caracteres")
        String email,

        @Schema(
                description = "Tipo de suministro contratado por el cliente",
                example = "residencial",
                allowableValues = {"residencial", "empresa", "industrial"},
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El tipo de cliente es obligatorio")
        @Pattern(
                regexp = "^(?i)(residencial|empresa|industrial)$",
                message = "El tipo debe ser 'residencial', 'empresa' o 'industrial'"
        )
        String tipo,

        @Schema(
                description = "Ciudad donde se ubica el punto de suministro",
                example = "Cartagena",
                maxLength = 50
        )
        @Size(max = 50, message = "La ciudad no puede superar los 50 caracteres")
        String ciudad,

        @Schema(
                description = "Fecha de registro del cliente en el sistema (YYYY-MM-DD)",
                example = "2026-09-14",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La fecha de alta es obligatoria")
        @PastOrPresent(message = "La fecha de alta no puede ser futura")
        LocalDate fechaAlta,

        @Schema(
                description = "Categoría o fidelización asignada al cliente",
                example = "NUEVO",
                allowableValues = {"NUEVO", "REGULAR", "PREMIUM", "VIP"}
        )
        @Pattern(
                regexp = "^(?i)(NUEVO|REGULAR|PREMIUM|VIP)$",
                message = "El segmento debe ser 'NUEVO', 'REGULAR', 'PREMIUM' o 'VIP'"
        )
        String segmento
) {
}