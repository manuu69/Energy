package org.example.energy.dto.incidencia;

import org.example.energy.enums.EstadoIncidencia;
import org.example.energy.enums.TipoIncidencia;

import java.time.LocalDate;

public record IncidenciaResponseDTO(
        Integer incidenciaId,
        Integer contratoId,
        TipoIncidencia tipo,
        LocalDate fechaApertura,
        LocalDate fechaCierre,
        EstadoIncidencia estado
) {
}
