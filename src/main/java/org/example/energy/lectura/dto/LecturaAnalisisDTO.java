package org.example.energy.lectura.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LecturaAnalisisDTO (
        Integer lecturaId,
        Integer contratoId,
        LocalDate fecha,
        BigDecimal consumoKwh,
        BigDecimal diferenciaConMedia
) {
}
