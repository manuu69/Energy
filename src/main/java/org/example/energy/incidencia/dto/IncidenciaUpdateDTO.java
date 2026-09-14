package org.example.energy.incidencia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.example.energy.common.enums.TipoIncidencia;

@Schema(description = "Datos para la actualización del tipo o categoría de una incidencia existente")
public record IncidenciaUpdateDTO(

        @Schema(
                description = "Nuevo tipo o reclasificación de la incidencia",
                example = "AVERIA_CONTADOR",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El tipo de incidencia es obligatorio")
        TipoIncidencia tipo
) {
}