package org.example.energy.zona.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para la actualización de los parámetros de una zona existente")
public record ZonaUpdateDTO(

        @Schema(
                description = "Nuevo nombre de la zona",
                example = "Zona Levante - Sector Norte",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,

        @Schema(
                description = "Nivel o jerarquía actualizada",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El nivel es obligatorio")
        Integer nivel,

        @Schema(
                description = "Descripción actualizada de la zona",
                example = "Ampliación de red para cubrir el sector industrial norte.",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 200
        )
        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
        String descripcion
) {
}