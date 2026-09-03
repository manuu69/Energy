package org.example.energy.dto.factura;

import org.example.energy.enums.EstadoPago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FacturaResponseDTO(

        Integer facturaId,
        Integer contratoId,
        LocalDate fechaEmision,
        BigDecimal importe,
        EstadoPago estadoPago,
        LocalDate fechaVencimiento,
        LocalDate fechaPago

) {
}
