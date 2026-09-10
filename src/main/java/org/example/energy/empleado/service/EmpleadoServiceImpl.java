package org.example.energy.empleado.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.common.enums.Departamento;
import org.example.energy.common.enums.RolEmpleado;
import org.example.energy.common.exception.code.ErrorCode;
import org.example.energy.common.exception.type.BusinessRuleException;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.empleado.dto.EmpleadoCreateDTO;
import org.example.energy.empleado.dto.EmpleadoResponseDTO;
import org.example.energy.empleado.dto.EmpleadoUpdateDTO;
import org.example.energy.empleado.entity.Empleado;
import org.example.energy.empleado.mapper.EmpleadoMapper;
import org.example.energy.empleado.repository.EmpleadoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    @Transactional(readOnly = true)
    public Page<EmpleadoResponseDTO> getAll(Pageable pageable) {
        Page<Empleado> empleados = empleadoRepository.findAll(pageable);
        return empleados.map(empleadoMapper::toDTO);
    }

    /**
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public EmpleadoResponseDTO getById(Integer id) {
        Empleado empleado = findEmpleadoById(id);
        return empleadoMapper.toDTO(empleado);
    }

    /**
     * @param rol
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> getByRol(RolEmpleado rol) {
        List<Empleado> empleados = empleadoRepository.findByRol(rol);
        return empleados.stream().map(empleadoMapper::toDTO).toList();
    }

    /**
     * @param departamento
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> getByDepartamento(Departamento departamento) {
        List<Empleado> empleados = empleadoRepository.findByDepartamento(departamento);
        return empleados.stream().map(empleadoMapper::toDTO).toList();
    }

    /**
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> getBySubordinados(Integer id) {
        List<Empleado> empleados = empleadoRepository.findSubordinadosByJefeId(id);
        return empleados.stream().map(empleadoMapper::toDTO).toList();
    }

    /**
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public EmpleadoResponseDTO create(EmpleadoCreateDTO dto) {
        Empleado empleado = empleadoMapper.toEntity(dto);

        if (dto.jefeId() != null) {
            Empleado jefe = empleadoRepository.findById(dto.jefeId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No existe el empleado jefe con ID: " + dto.jefeId()
                    ));
            empleado.setJefe(jefe);
        }

        if (dto.rol() == RolEmpleado.DIRECTOR_GENERAL) {
            boolean existeDirector = empleadoRepository.existsByRol(RolEmpleado.DIRECTOR_GENERAL);
            if (existeDirector) {
                throw new IllegalArgumentException("Ya existe un Director General registrado en el sistema.");
            }
        }

        empleado.setActivo(true);
        empleado.setFechaAlta(LocalDate.now());

        Empleado saved = empleadoRepository.save(empleado);
        return empleadoMapper.toDTO(saved);
    }

    /**
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public EmpleadoResponseDTO update(Integer id, EmpleadoUpdateDTO dto) {
        Empleado empleado = findEmpleadoById(id);

        empleadoMapper.updateEntityFromDto(dto, empleado);
        return empleadoMapper.toDTO(empleado);
    }

    /**
     * @param id
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        Empleado empleado = findEmpleadoById(id);

        if (empleadoRepository.existsByJefeEmpleadoIdAndActivoTrue(id)) {
            throw new IllegalStateException(
                    "No se puede desactivar al empleado porque tiene subordinados activos. Reasigne el equipo primero."
            );
        }

        empleado.setActivo(false);

    }

    private Empleado findEmpleadoById(Integer id){
        return empleadoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Empleado no encontrado con id={}", id);

                    return new ResourceNotFoundException("Empelado no encontrado con el id: " + id);
                });
    }
}
