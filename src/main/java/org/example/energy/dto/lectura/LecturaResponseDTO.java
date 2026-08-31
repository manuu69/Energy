package org.example.energy.dto.lectura;

import org.example.energy.enums.TipoLectura;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LecturaResponseDTO(
        Integer lecuraId,
        Integer contratoId,
        LocalDate fechaLectura,
        BigDecimal consumoKwh,
        TipoLectura tipoLectura
) {
}
