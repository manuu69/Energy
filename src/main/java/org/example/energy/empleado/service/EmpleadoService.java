package org.example.energy.empleado.service;

import org.example.energy.common.enums.Departamento;
import org.example.energy.common.enums.RolEmpleado;
import org.example.energy.empleado.dto.EmpleadoCreateDTO;
import org.example.energy.empleado.dto.EmpleadoResponseDTO;
import org.example.energy.empleado.dto.EmpleadoUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmpleadoService {
    Page<EmpleadoResponseDTO> getAll(Pageable pageable);
    EmpleadoResponseDTO getById(Integer id);
    List<EmpleadoResponseDTO> getByRol(RolEmpleado rol);
    List<EmpleadoResponseDTO> getByDepartamento(Departamento departamento);
    List<EmpleadoResponseDTO> getBySubordinados(Integer id);
    EmpleadoResponseDTO create(EmpleadoCreateDTO dto);
    EmpleadoResponseDTO update(Integer id, EmpleadoUpdateDTO dto);
    void delete(Integer id);
}
