package org.example.energy.contrato.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.energy.common.enums.TipoTarifa;

import java.math.BigDecimal;

@Schema(description = "Datos para la modificación de las condiciones de un contrato activo")
public record ContratoUpdateDTO(

        @Schema(
                description = "Identificador de la nueva zona de distribución asignada",
                example = "4",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        Integer zonaId,

        @Schema(
                description = "Nueva tarifa eléctrica aplicada al contrato",
                example = "PLAN_STABLE",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        TipoTarifa tarifa,

        @Schema(
                description = "Nueva potencia contratada en kilovatios (kW)",
                example = "5.75",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        @Positive
        BigDecimal potenciaKw
) {
}