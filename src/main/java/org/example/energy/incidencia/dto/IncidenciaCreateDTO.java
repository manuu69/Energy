package org.example.energy.incidencia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.energy.common.enums.TipoIncidencia;

@Schema(description = "Datos necesarios para el registro de una nueva incidencia en el sistema")
public record IncidenciaCreateDTO(

        @Schema(
                description = "Identificador único del contrato asociado a la incidencia",
                example = "12",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El contrato es obligatorio")
        @Positive(message = "El identificador del contrato debe ser positivo")
        Integer contratoId,

        @Schema(
                description = "Tipo o categoría de la incidencia reportada",
                example = "CORTE_SUMINISTRO",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El tipo de incidencia es obligatorio")
        TipoIncidencia tipo
) {
}