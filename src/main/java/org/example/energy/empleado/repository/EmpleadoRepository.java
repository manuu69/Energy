package org.example.energy.empleado.repository;

import org.example.energy.common.enums.Departamento;
import org.example.energy.common.enums.RolEmpleado;
import org.example.energy.empleado.entity.Empleado;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    List<Empleado> findByRol(RolEmpleado rol);

    List<Empleado> findByDepartamento(Departamento departamento);

    @Query("SELECT e FROM Empleado e WHERE e.jefe.empleadoId = :jefeId")
    List<Empleado> findSubordinadosByJefeId(@Param("jefeId") Integer jefeId);

    boolean existsById(@NonNull Integer id);
    boolean existsByEmail(@NonNull String email);

    boolean existsByRol(RolEmpleado rolEmpleado);

    boolean existsByJefeEmpleadoIdAndActivoTrue(Integer jefeId);
}
