package org.example.energy.dto;

import org.example.energy.enums.EstadoPago;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FacturaResponseDTO(

        Integer facturaId,
        Integer contratoId,
        LocalDate fechaEmision,
        BigDecimal importe,
        EstadoPago estadoPago,
        LocalDate fechaVencimiento

) {
}
