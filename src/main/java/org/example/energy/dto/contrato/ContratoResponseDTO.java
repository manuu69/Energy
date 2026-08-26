package org.example.energy.dto.contrato;

import org.example.energy.enums.EstadoContrato;
import org.example.energy.enums.TipoTarifa;

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
