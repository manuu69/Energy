package org.example.energy.incidencia.dto;

import jakarta.validation.constraints.NotNull;
import org.example.energy.common.enums.TipoIncidencia;

public record IncidenciaUpdateDTO(
        @NotNull(message = "El tipo de incidencia es obligatorio")
        TipoIncidencia tipo
) {
}
