package org.example.energy.contrato.dto;

import org.example.energy.common.enums.EstadoContrato;
import org.example.energy.common.enums.TipoTarifa;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratoResponseDTO(
        Integer contratoId,
        Integer clienteId,
        Integer zonaId,
        TipoTarifa tarifa,
        BigDecimal potenciaKw,
        LocalDate fechaInicio,
        EstadoContrato estado
) {
}
