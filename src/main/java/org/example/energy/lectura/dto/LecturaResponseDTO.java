package org.example.energy.lectura.dto;

import org.example.energy.common.enums.TipoLectura;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LecturaResponseDTO(
        Integer lecturaId,
        Integer contratoId,
        LocalDate fecha,
        BigDecimal consumoKwh,
        TipoLectura tipoLectura
) {
}
