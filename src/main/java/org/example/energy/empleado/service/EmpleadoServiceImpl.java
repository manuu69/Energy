package org.example.energy.empleado.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.common.enums.Departamento;
import org.example.energy.common.enums.RolEmpleado;
import org.example.energy.empleado.dto.EmpleadoCreateDTO;
import org.example.energy.empleado.dto.EmpleadoResponseDTO;
import org.example.energy.empleado.dto.EmpleadoUpdateDTO;
import org.example.energy.empleado.mapper.EmpleadoMapper;
import org.example.energy.empleado.repository.EmpleadoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService
{

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper empleadoMapper;


    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<EmpleadoResponseDTO> getAll(Pageable pageable) {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public EmpleadoResponseDTO getById(Integer id) {
        return null;
    }

    /**
     * @param rol
     * @return
     */
    @Override
    public List<EmpleadoResponseDTO> getByRol(RolEmpleado rol) {
        return List.of();
    }

    /**
     * @param departamento
     * @return
     */
    @Override
    public List<EmpleadoResponseDTO> getByDepartamento(Departamento departamento) {
        return List.of();
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<EmpleadoResponseDTO> getBySubordinados(Integer id) {
        return List.of();
    }

    /**
     * @param dto
     * @return
     */
    @Override
    public EmpleadoResponseDTO create(EmpleadoCreateDTO dto) {
        return null;
    }

    /**
     * @param dto
     * @return
     */
    @Override
    public EmpleadoResponseDTO update(EmpleadoUpdateDTO dto) {
        return null;
    }

    /**
     * @param id
     */
    @Override
    public void delete(Integer id) {

    }
}
