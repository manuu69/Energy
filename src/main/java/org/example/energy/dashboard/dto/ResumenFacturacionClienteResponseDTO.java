package org.example.energy.dashboard.dto;

import java.math.BigDecimal;

public record ResumenFacturacionClienteResponseDTO (
        Integer clienteId,
        String nombre,
        Long cantidadContratosActivos,
        BigDecimal totalFacturado,
        Long facturasPendientes
) {
}
