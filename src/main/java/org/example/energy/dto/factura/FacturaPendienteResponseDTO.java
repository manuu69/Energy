package org.example.energy.dto.factura;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FacturaPendienteResponseDTO(
         Integer facturaId,
         Integer contratoId,
         LocalDate fechaEmision,
         BigDecimal importe,
         String estadoPago, // Ej: "PENDIENTE", "PAGADA", "CANCELADA"
         LocalDate fechaVencimiento
) {
}
