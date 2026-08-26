package org.example.energy.dto.contrato;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.energy.enums.TipoTarifa;

import java.math.BigDecimal;

public record ContratoUpdateDTO(
        @NotNull Integer zonaId,
        @NotNull TipoTarifa tarifa,
        @NotNull @Positive BigDecimal potenciaKw
) {
}
