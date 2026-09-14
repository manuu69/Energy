package org.example.energy.lectura.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.example.energy.common.enums.TipoLectura;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Datos necesarios para el registro de una nueva lectura de contador")
public record LecturaCreateDTO (

        @Schema(
                description = "Identificador único del contrato asociado",
                example = "12",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El ID de contrato es obligatorio")
        @Positive(message = "El ID de contrato debe ser un entero positivo")
        Integer contratoId,

        @Schema(
                description = "Fecha en la que se realiza la lectura (YYYY-MM-DD)",
                example = "2026-09-14",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La fecha de lectura es obligatoria")
        @PastOrPresent(message = "La fecha de lectura no puede ser en el futuro")
        LocalDate fecha,

        @Schema(
                description = "Consumo registrado expresado en kilovatios-hora (kWh)",
                example = "245.80",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El consumo en kWh es obligatorio")
        @Positive(message = "El consumo en kWh debe ser un valor mayor que 0")
        BigDecimal consumoKwh,

        @Schema(
                description = "Método o vía por la que se ha obtenido la lectura",
                example = "REAL",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El tipo de lectura es obligatorio")
        TipoLectura tipoLectura
){
}