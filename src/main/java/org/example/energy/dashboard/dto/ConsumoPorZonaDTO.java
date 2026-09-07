package org.example.energy.dashboard.dto;

import java.math.BigDecimal;

public record ConsumoPorZonaDTO(
        String zona,
        Integer nivel,
        BigDecimal consumoTotal,
        Long numContratos
) {
}
