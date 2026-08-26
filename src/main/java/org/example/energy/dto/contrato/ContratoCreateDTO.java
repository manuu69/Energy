package org.example.energy.dto.contrato;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.energy.enums.EstadoContrato;
import org.example.energy.enums.TipoTarifa;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratoCreateDTO(

        @NotNull(message = "El cliente es obligatorio")
        @Positive(message = "El cliente debe tener un identificador válido")
        Integer clienteId,

        @NotNull(message = "La tarifa es obligatoria")
        TipoTarifa tarifa,

        @NotNull(message = "La potencia contratada es obligatoria")
        @DecimalMin(
                value = "0.1",
                message = "La potencia contratada debe ser mayor que cero"
        )
        BigDecimal potenciaKw,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        @NotNull(message = "La zona es obligatoria")
        @Positive(message = "La zona debe tener un identificador válido")
        Integer zonaId
) {
}
