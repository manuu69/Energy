package org.example.energy.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FacturacionMensualDTO (
        LocalDateTime mes,
        BigDecimal totalFacturado,
        BigDecimal totalCobrado

) {
}
