package org.example.energy.dto.factura;

import org.example.energy.enums.EstadoPago;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FacturaPendienteResponseDTO(
         Integer facturaId,
         Integer contratoId,
         LocalDate fechaEmision,
         BigDecimal importe,
         EstadoPago estadoPago,
         LocalDate fechaVencimiento
) {
}
