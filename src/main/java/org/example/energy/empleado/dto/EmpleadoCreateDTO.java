package org.example.energy.empleado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.energy.common.enums.Departamento;
import org.example.energy.common.enums.RolEmpleado;

import java.math.BigDecimal;

@Schema(description = "Objeto con los datos necesarios para registrar un nuevo empleado")
public record EmpleadoCreateDTO (
        @Schema(description = "Nombre completo del empleado", example = "Laura Gómez", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,

        @Schema(description = "Correo corporativo del empleado", example = "laura.gomez@empresa.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @Email(message = "Debe proporcionar un email válido")
        @NotBlank
        String email,

        @Schema(description = "Departamento al que pertenece", example = "DESARROLLO")
        @NotNull
        Departamento departamento,

        @Schema(description = "Rol o nivel de acceso dentro del sistema", example = "TECNICO")
        @NotNull
        RolEmpleado rol,

        @Schema(description = "Salario del empleado")
        @Positive(message = "El salario debe ser mayor que cero")
        @NotNull(message = "El salario no debe ser nulo")
        BigDecimal salario,

        Integer jefeId
) {
}
