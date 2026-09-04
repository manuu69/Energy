package org.example.energy.incidencia.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.energy.common.enums.TipoIncidencia;

public record IncidenciaCreateDTO(

        @NotNull(message = "El contrato es obligatorio")
        @Positive(message = "El identificador del contrato debe ser positivo")
        Integer contratoId,

        @NotNull(message = "El tipo de incidencia es obligatorio")
        TipoIncidencia tipo
) {
}

