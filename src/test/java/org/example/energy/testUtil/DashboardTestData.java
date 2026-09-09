package org.example.energy.testUtil;

import org.example.energy.common.enums.TipoCliente;
import org.example.energy.dashboard.dto.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class DashboardTestData {

    private DashboardTestData() {
    }

    public static DashboardResumenDTO crearDashboardResumenDTO() {
        return new DashboardResumenDTO(
                150L,
                180L,
                12L,
                new BigDecimal("4500.50"),
                5L,
                2L,
                new BigDecimal("12500.00"),
                new BigDecimal("8900.00"),
                new BigDecimal("8200.00"),
                new BigDecimal("50000.00"),
                new BigDecimal("7500.00"),
                3L,
                4L,
                new BigDecimal("1200.00"),
                1L
        );
    }

    public static TopDeudorDTO crearTopDeudorDTO() {
        return new TopDeudorDTO(
                "Empresa Ficticia S.L.",
                new BigDecimal("3200.00")
        );
    }

    public static FacturacionMensualDTO crearFacturacionMensualDTO() {
        return new FacturacionMensualDTO(
                LocalDateTime.of(2026, 5, 1, 0, 0),
                new BigDecimal("15000.00"),
                new BigDecimal("12000.00")
        );
    }

    public static IncidenciaPorTipoDTO crearIncidenciaPorTipoDTO() {
        return new IncidenciaPorTipoDTO(
                "AVERIA",
                10L,
                3L,
                2L,
                5L
        );
    }

    public static ConsumoPorZonaDTO crearConsumoPorZonaDTO() {
        return new ConsumoPorZonaDTO(
                "Zona Centro",
                1,
                new BigDecimal("45000.75"),
                85L
        );
    }
}