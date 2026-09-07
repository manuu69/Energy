package org.example.energy.dashboard.dto;

import java.math.BigDecimal;

public record TopDeudorDTO (
        String nombre,
        BigDecimal deudaTotal
) {
}
