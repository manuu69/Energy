package org.example.energy.cliente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.example.energy.common.enums.Segmento;
import org.example.energy.common.enums.TipoCliente;

@Schema(description = "Datos para la actualización del perfil de un cliente existente")
public record ClienteUpdateDTO(

        @Schema(
                description = "Nombre completo o razón social actualizada",
                example = "Carlos Pérez Gómez",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 100
        )
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,

        @Schema(
                description = "Correo electrónico actualizado",
                example = "carlos.perez.nuevo@example.com",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 150
        )
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        @Size(max = 150, message = "El email no puede superar los 150 caracteres")
        String email,

        @Schema(
                description = "Clasificación del tipo de cliente",
                example = "EMPRESA",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El tipo de cliente es obligatorio")
        TipoCliente tipo,

        @Schema(
                description = "Ciudad del punto de suministro",
                example = "Murcia",
                maxLength = 50
        )
        @Size(max = 50, message = "La ciudad no puede superar los 50 caracteres")
        String ciudad,

        @Schema(
                description = "Segmento comercial asignado",
                example = "PREMIUM"
        )
        Segmento segmento
) {
}