package org.example.energy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FacturaResponseDTO(

        Integer facturaId,
        Integer contratoId,
        LocalDate fechaEmision,
        BigDecimal importe,
        String estadoPago,
        LocalDate fechaVencimiento

) {
}
