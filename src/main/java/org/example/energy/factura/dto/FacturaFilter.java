package org.example.energy.factura.dto;

import org.example.energy.common.enums.EstadoPago;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FacturaFilter(
        Integer contratoId,
        Integer clienteId,
        EstadoPago estadoPago,
        LocalDate fechaEmisionDesde,
        LocalDate fechaEmisionHasta,
        LocalDate fechaVencimientoHasta,
        BigDecimal importeMin,
        BigDecimal importeMax
) {}
