package org.example.energy.testUtil;

import org.example.energy.dashboard.dto.ResumenFacturacionClienteResponseDTO;

import java.math.BigDecimal;

public final class ResumenFacturacionClienteTestData {

    private ResumenFacturacionClienteTestData() {
    }

    public static ResumenFacturacionClienteResponseDTO crearResumenFacturacionClienteResponseDTO() {
        return new ResumenFacturacionClienteResponseDTO(
                1,
                "Empresa Ejemplo S.A.",
                3L,
                new BigDecimal("12500.50"),
                2L
        );
    }
}
