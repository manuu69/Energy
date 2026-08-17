package org.example.energy.dto;

import jakarta.validation.constraints.*;

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

        @NotBlank(message = "El estado de pago es obligatorio")
        @Pattern(
                regexp = "^(pendiente|pagada|cancelada)$",
                message = "El estado de pago debe ser 'PENDIENTE', 'pagada' o 'cancelada'"
        )
        String estadoPago,

        @NotNull(message = "La fecha de vencimiento es obligatoria")
        LocalDate fechaVencimiento

) {
}
