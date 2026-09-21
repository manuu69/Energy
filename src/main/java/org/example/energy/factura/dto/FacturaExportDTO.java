package org.example.energy.factura.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

public record FacturaExportDTO(
        Integer facturaId,
        Integer contratoId,
        Integer clienteId,
        String nombreCliente,
        LocalDate fechaEmision,
        BigDecimal importe,
        String estadoPago,
        LocalDate fechaVencimiento
) {
    public String toCsvLine() {
        return String.format(Locale.US,
                "%d;%d;%d;%s;%s;%.2f;%s;%s",
                facturaId,
                contratoId,
                clienteId,
                nombreCliente,
                fechaEmision,
                importe,
                estadoPago,
                fechaVencimiento
        );
    }
}
