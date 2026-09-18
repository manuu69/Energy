package org.example.energy.contrato.dto;

import org.example.energy.common.enums.EstadoContrato;
import org.example.energy.common.enums.TipoTarifa;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratoFilter(
        Integer clienteId,
        TipoTarifa tarifa,
        EstadoContrato estado,
        Integer zonaId,
        LocalDate fechaInicioDesde,
        LocalDate fechaInicioHasta,
        BigDecimal potenciaMin,
        BigDecimal potenciaMax
) {}
