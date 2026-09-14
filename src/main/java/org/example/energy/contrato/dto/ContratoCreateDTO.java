package org.example.energy.contrato.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.energy.common.enums.TipoTarifa;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Datos necesarios para la creación de un nuevo contrato de suministro eléctrico")
public record ContratoCreateDTO(

        @Schema(
                description = "Identificador único del cliente titular del contrato",
                example = "105",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @JsonProperty
        @NotNull(message = "El cliente es obligatorio")
        @Positive(message = "El cliente debe tener un identificador válido")
        Integer clienteId,

        @Schema(
                description = "Tipo de tarifa eléctrica asociada al contrato",
                example = "PLAN_NOCHE",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @JsonProperty
        @NotNull(message = "La tarifa es obligatoria")
        TipoTarifa tarifa,

        @Schema(
                description = "Potencia eléctrica contratada expresada en kilovatios (kW)",
                example = "4.6",
                minimum = "0.1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @JsonProperty
        @NotNull(message = "La potencia contratada es obligatoria")
        @DecimalMin(
                value = "0.1",
                inclusive = true,
                message = "La potencia contratada debe ser igual o mayor que 0.1"
        )
        BigDecimal potenciaKw,

        @Schema(
                description = "Fecha de entrada en vigor del contrato (YYYY-MM-DD)",
                example = "2026-09-14",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @JsonProperty
        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        @Schema(
                description = "Identificador de la zona de distribución eléctrica asignada",
                example = "3",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @JsonProperty
        @NotNull(message = "La zona es obligatoria")
        @Positive(message = "La zona debe tener un identificador válido")
        Integer zonaId

) {
}