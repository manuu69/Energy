package org.example.energy.testUtil;

import org.example.energy.common.enums.Departamento;
import org.example.energy.common.enums.RolEmpleado;
import org.example.energy.empleado.dto.EmpleadoCreateDTO;
import org.example.energy.empleado.dto.EmpleadoResponseDTO;
import org.example.energy.empleado.dto.EmpleadoUpdateDTO;
import org.example.energy.empleado.entity.Empleado;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmpleadoTestData {

    public static Empleado crearJefe() {
        Empleado jefe = new Empleado();
        jefe.setEmpleadoId(1);
        jefe.setNombre("Jefe Director");
        jefe.setActivo(true);
        return jefe;
    }

    public static Empleado crearEmpleado() {
        Empleado empleado = new Empleado();
        empleado.setEmpleadoId(2);
        empleado.setNombre("Carlos Mendoza");
        empleado.setEmail("carlos@empresa.com");
        empleado.setDepartamento(Departamento.OPERACIONES);
        empleado.setRol(RolEmpleado.DBA);
        empleado.setSalario(new BigDecimal("30000.00"));
        empleado.setFechaAlta(LocalDate.now());
        empleado.setActivo(true);
        empleado.setJefe(crearJefe());
        return empleado;
    }

    public static EmpleadoCreateDTO crearEmpleadoCreateDTO(Integer jefeId) {
        return new EmpleadoCreateDTO(
                "Carlos Mendoza",
                "carlos@empresa.com",
                Departamento.OPERACIONES,
                RolEmpleado.DBA,
                new BigDecimal("30000.00"),
                jefeId
        );
    }

    public static EmpleadoUpdateDTO crearEmpleadoUpdateDTO() {
        return new EmpleadoUpdateDTO(
                "carlos.nuevo@empresa.com",
                Departamento.COMERCIAL,
                RolEmpleado.CONTABLE,
                new BigDecimal("35000.00"),
                4
        );
    }

    public static EmpleadoResponseDTO crearEmpleadoResponseDTO() {
        return new EmpleadoResponseDTO(
                2,
                "Carlos Mendoza",
                "carlos@empresa.com",
                Departamento.OPERACIONES,
                RolEmpleado.DESARROLLADOR_JUNIOR,
                new BigDecimal("30000.00"),
                LocalDate.now(),
                true,
                1
        );
    }
}