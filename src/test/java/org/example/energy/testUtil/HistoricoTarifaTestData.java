package org.example.energy.testUtil;

import org.example.energy.common.enums.TipoTarifa;
import org.example.energy.historico_tarifa.dto.HistoricoTarifaResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class HistoricoTarifaTestData {

    private HistoricoTarifaTestData() {
    }

    public static HistoricoTarifaResponseDTO crearHistoricoTarifaResponseDTO() {
        return new HistoricoTarifaResponseDTO(
                1,
                10,
                TipoTarifa.TARIFA_2_0_A,
                new BigDecimal("0.150000"),
                new BigDecimal("10.00"),
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                "Revisión anual de precios"
        );
    }
}