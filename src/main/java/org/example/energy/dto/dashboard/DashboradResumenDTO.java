package org.example.energy.dto.dashboard;

import java.math.BigDecimal;

public record DashboradResumenDTO(
        Long clientesActivos,
        Long contratosActivos,
        Long facturasPendientes,
        BigDecimal importePendiente,
        Long incidenciasAbiertas,
        Long incidenciasEnGestion,
        BigDecimal cosumoMesActual
) {
}
