package org.example.energy.historico_tarifa.dto;

import org.example.energy.common.enums.TipoTarifa;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HistoricoTarifaResponseDTO(
        Integer historicoId,
        Integer contratoId,
        TipoTarifa tarifa,
        BigDecimal precioKwh,
        BigDecimal potenciaKw,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String motivoCambio
) {
}
