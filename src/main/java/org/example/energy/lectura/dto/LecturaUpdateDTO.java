package org.example.energy.lectura.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.example.energy.common.enums.TipoLectura;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Datos para la modificación de una lectura de contador registrada previamente")
public record LecturaUpdateDTO(

        @Schema(
                description = "Nueva fecha corregida para la lectura (YYYY-MM-DD)",
                example = "2026-09-10",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La fecha de lectura es obligatoria")
        LocalDate fechaLectura,

        @Schema(
                description = "Valor del consumo corregido expresado en kWh",
                example = "250.00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El consumo es obligatorio")
        @PositiveOrZero(message = "El consumo no puede ser negativo")
        BigDecimal consumoKwh,

        @Schema(
                description = "Tipo de lectura actualizado",
                example = "ESTIMADA",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Debe de introducir un tipo de lectura")
        TipoLectura tipoLectura
) {
}