package org.example.energy.incidencia.dto;

import org.example.energy.common.enums.EstadoIncidencia;
import org.example.energy.common.enums.TipoIncidencia;

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
