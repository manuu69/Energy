package org.example.energy.dto.factura;

import jakarta.validation.constraints.*;
import org.example.energy.enums.EstadoPago;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FacturaCreateDTO(

        @NotNull(message = "El ID de contrato es obligatorio")
        @Positive(message = "El ID de contrato debe ser un entero positivo")
        Integer contratoId,

        @NotNull(message = "La fecha de emisión es obligatoria")
        @PastOrPresent(message = "La fecha de emisión no puede ser futura")
        LocalDate fechaEmision,

        @NotNull(message = "El importe es obligatorio")
        @Positive(message = "El importe debe ser superior a 0.00")
        BigDecimal importe,

        @NotNull
        EstadoPago estadoPago,

        @NotNull(message = "La fecha de vencimiento es obligatoria")
        LocalDate fechaVencimiento

) {
}
