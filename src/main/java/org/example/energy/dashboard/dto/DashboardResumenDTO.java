package org.example.energy.dashboard.dto;

import java.math.BigDecimal;

public record DashboardResumenDTO(
        Long clientesActivos,
        Long contratosActivos,
        Long facturasPendientes,
        BigDecimal importePendiente,
        Long incidenciasAbiertas,
        Long incidenciasEnGestion,
        BigDecimal consumoMesActual,
        BigDecimal facturacionMesActual,
        BigDecimal facturacionMesAnterior,
        BigDecimal importeCobradoTotal,
        BigDecimal importeCobradoMesActual,
        Long facturasVencidas,
        Long clientesConDeuda,
        BigDecimal importeVencido,
        Long incidenciasCriticas
) {
}
