package org.example.energy.dto.incidencia;

import jakarta.validation.constraints.NotNull;
import org.example.energy.enums.TipoIncidencia;

public record IncidenciaUpdateDTO(
        @NotNull(message = "El tipo de incidencia es obligatorio")
        TipoIncidencia tipo
) {
}
