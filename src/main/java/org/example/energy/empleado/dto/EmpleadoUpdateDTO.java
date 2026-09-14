package org.example.energy.empleado.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.energy.common.enums.Departamento;
import org.example.energy.common.enums.RolEmpleado;

import java.math.BigDecimal;

@Schema(description = "Objeto con los datos necesarios para actualizar un empleado")
public record EmpleadoUpdateDTO(

        @Schema(description = "Correo corporativo del empleado", example = "laura.gomez@empresa.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @Email(message = "Formato de email inválido")
        @NotNull(message = "El email no debe estar vacío")
        String email,

        @Schema(description = "Departamento al que pertenece", example = "DESARROLLO")
        @NotNull(message = "El departamento no debe estar vacío")
        Departamento departamento,

        @Schema(description = "Rol o nivel de acceso dentro del sistema", example = "TECNICO")
        @NotNull(message = "El rol no debe estar vacío")
        RolEmpleado rol,

        @Schema(description = "Salario del empleado")
        @Positive(message = "El salario debe ser mayor que cero")
        @NotNull(message = "El salario no debe ser nulo")
        BigDecimal salario,

        Integer jefeId
) {
}