package org.example.energy.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LecturaCreateDTO (

        @NotNull(message = "El ID de contrato es obligatorio")
        @Positive(message = "El ID de contrato debe ser un entero positivo")
        Integer contratoId,

        @NotNull(message = "La fecha de lectura es obligatoria")
        @PastOrPresent(message = "La fecha de lectura no puede ser en el futuro")
        LocalDate fechaLectura,

        @NotNull(message = "El consumo en kWh es obligatorio")
        @Positive(message = "El consumo en kWh debe ser un valor mayor que 0")
        BigDecimal consumoKwh,

        @NotNull(message = "El tipo de lectura es obligatorio")
        String tipoLectura
){
}
