package org.example.energy.factura.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Datos requeridos para la emision de una nueva factura asociada a un contrato")
public record FacturaCreateDTO(

        @Schema(
                description = "Identificador del contrato al que pertenece la factura",
                example = "12",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El ID de contrato es obligatorio")
        @Positive(message = "El ID de contrato debe ser un entero positivo")
        Integer contratoId,

        @Schema(
                description = "Fecha en la que se emite la factura (YYYY-MM-DD)",
                example = "2026-09-01",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La fecha de emisión es obligatoria")
        @PastOrPresent(message = "La fecha de emisión no puede ser futura")
        LocalDate fechaEmision,

        @Schema(
                description = "Monto total a pagar expresado en euros (€)",
                example = "84.50",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El importe es obligatorio")
        @Positive(message = "El importe debe ser superior a 0.00")
        BigDecimal importe,

        @Schema(
                description = "Fecha limite para realizar el pago (YYYY-MM-DD)",
                example = "2026-10-01",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La fecha de vencimiento es obligatoria")
        @FutureOrPresent(message = "La fecha de vencimiento debe ser hoy o una fecha posterior a hoy")
        LocalDate fechaVencimiento

) {
}