package org.example.energy.empleado.dto;

import org.example.energy.common.enums.Departamento;
import org.example.energy.common.enums.RolEmpleado;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmpleadoResponseDTO(
        Integer empleadoId,
        String nombre,
        String email,
        Departamento departamento,
        RolEmpleado rol,
        BigDecimal salario,
        LocalDate fechaAlta,
        Integer jefeId
) {
}
