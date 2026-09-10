package org.example.energy.empleado.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.energy.common.enums.Departamento;
import org.example.energy.common.enums.RolEmpleado;

import java.math.BigDecimal;

public record EmpleadoUpdateDTO(

        @Email(message = "Formato de email inválido")
        @NotNull(message = "El email no debe estar vacío")
        String email,

        @NotNull(message = "El departamento no debe estar vacío")
        Departamento departamento,

        @NotNull(message = "El rol no debe estar vacío")
        RolEmpleado rol,

        @Positive(message = "El salario debe ser mayor que cero")
        @NotNull(message = "El salario no debe ser nulo")
        BigDecimal salario
) {
}