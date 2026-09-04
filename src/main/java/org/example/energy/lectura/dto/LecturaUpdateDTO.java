package org.example.energy.lectura.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.example.energy.common.enums.TipoLectura;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LecturaUpdateDTO(

        @NotNull(message = "La fecha de lectura es obligatoria")
        LocalDate fechaLectura,

        @NotNull(message = "El consumo es obligatorio")
        @PositiveOrZero(message = "El consumo no puede ser negativo")
        BigDecimal consumoKwh,

        @NotNull(message = "Debe de introducir un tipo de lectura")
        TipoLectura tipoLectura
) {
}
