package org.example.energy.factura.dto;

import org.example.energy.common.enums.EstadoPago;

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
