package org.example.energy.zona.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos requeridos para el registro de una nueva zona geográfica o de distribución")
public record ZonaCreateDTO(

        @Schema(
                description = "Nombre descriptivo o denominación de la zona",
                example = "Zona Levante - Sector Sur",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,

        @Schema(
                description = "Nivel de prioridad o jerarquía de la infraestructura en la zona",
                example = "2",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El nivel es obligatorio")
        Integer nivel,

        @Schema(
                description = "Detalles sobre la cobertura, municipio o características de la zona",
                example = "Cubre la distribución eléctrica del área metropolitana y polígonos circundantes.",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 200
        )
        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 200, message = "La descripción no puede superar los 200 caracteres")
        String descripcion
) {
}