package org.example.energy.empleado.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.energy.common.enums.Departamento;
import org.example.energy.common.enums.RolEmpleado;

import java.math.BigDecimal;

public record EmpleadoCreateDTO (
        @NotNull(message = "El nombre no debe de estar vacio")
        String nombre,

        @Email
        @NotNull(message = "El email no debe de estar vacio")
        String email,

        @NotBlank(message = "El departamento no debe de estar vacio")
        Departamento departamento,

        @NotBlank(message = "El rol no debe de estar vacio")
        RolEmpleado rol,

        @Positive
        @NotNull(message = "NO nulo")
        BigDecimal salario
) {
}
