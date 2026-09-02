package org.example.energy.dto.dashboard.resumen;

import java.math.BigDecimal;

public record ResumenFacturacionClienteResponseDTO (
        Integer clienteId,
        String nombre,
        Long cantidadContratosActivos,
        BigDecimal totalFacturado,
        Long facturasPendientes
) {
}
