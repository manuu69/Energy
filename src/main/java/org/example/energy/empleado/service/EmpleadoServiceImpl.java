package org.example.energy.empleado.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.common.enums.Departamento;
import org.example.energy.common.enums.RolEmpleado;
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
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper empleadoMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<EmpleadoResponseDTO> getAll(Pageable pageable) {
        log.debug(
                "Consultando empleados paginados. page={}, size={}, sort={}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<Empleado> empleados = empleadoRepository.findAll(pageable);

        log.info(
                "Consulta de empleados realizada. totalElements={}, totalPages={}, currentPage={}",
                empleados.getTotalElements(),
                empleados.getTotalPages(),
                empleados.getNumber()
        );

        return empleados.map(empleadoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public EmpleadoResponseDTO getById(Integer id) {
        log.debug("Buscando empleado con id={}", id);

        Empleado empleado = findEmpleadoById(id);
        return empleadoMapper.toDTO(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> getByRol(RolEmpleado rol) {
        log.debug("Consultando empleados por rol={}", rol);

        List<Empleado> empleados = empleadoRepository.findByRol(rol);

        log.info("Empleados encontrados para rol={}: total={}", rol, empleados.size());
        return empleados.stream().map(empleadoMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> getByDepartamento(Departamento departamento) {
        log.debug("Consultando empleados por departamento={}", departamento);

        List<Empleado> empleados = empleadoRepository.findByDepartamento(departamento);

        log.info("Empleados encontrados para departamento={}: total={}", departamento, empleados.size());
        return empleados.stream().map(empleadoMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> getBySubordinados(Integer id) {
        log.debug("Consultando subordinados para el jefe id={}", id);

        List<Empleado> empleados = empleadoRepository.findSubordinadosByJefeId(id);

        log.info("Subordinados encontrados para jefe id={}: total={}", id, empleados.size());
        return empleados.stream().map(empleadoMapper::toDTO).toList();
    }

    @Override
    @Transactional
    public EmpleadoResponseDTO create(EmpleadoCreateDTO dto) {
        log.info("Iniciando creación de empleado con rol={}", dto.rol());

        Empleado empleado = empleadoMapper.toEntity(dto);

        if (dto.jefeId() != null) {
            log.debug("Buscando jefe asociado con id={}", dto.jefeId());
            Empleado jefe = empleadoRepository.findById(dto.jefeId())
                    .orElseThrow(() -> {
                        log.warn("No se encontró el empleado jefe con id={}", dto.jefeId());
                        return new ResourceNotFoundException(
                                "No existe el empleado jefe con ID: " + dto.jefeId()
                        );
                    });
            empleado.setJefe(jefe);
        }

        if (dto.rol() == RolEmpleado.DIRECTOR_GENERAL) {
            log.debug("Verificando existencia previa de Director General");
            boolean existeDirector = empleadoRepository.existsByRol(RolEmpleado.DIRECTOR_GENERAL);
            if (existeDirector) {
                log.warn("Creación de empleado rechazada. Ya existe un Director General registrado");
                throw new IllegalArgumentException("Ya existe un Director General registrado en el sistema.");
            }
        }

        empleado.setActivo(true);
        empleado.setFechaAlta(LocalDate.now());

        Empleado saved = empleadoRepository.save(empleado);

        log.info("Empleado creado correctamente. empleadoId={}, rol={}", saved.getEmpleadoId(), saved.getRol());

        return empleadoMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public EmpleadoResponseDTO update(Integer id, EmpleadoUpdateDTO dto) {
        log.info("Iniciando actualización de empleado id={}", id);

        Empleado empleado = findEmpleadoById(id);

        log.debug("Empleado encontrado para actualización. empleadoId={}", id);

        empleadoMapper.updateEntityFromDto(dto, empleado);

        log.info("Empleado id={} actualizado correctamente", id);

        return empleadoMapper.toDTO(empleado);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Iniciando desactivación de empleado id={}", id);

        Empleado empleado = findEmpleadoById(id);

        if (empleadoRepository.existsByJefeEmpleadoIdAndActivoTrue(id)) {
            log.warn("Desactivación rechazada. El empleado id={} tiene subordinados activos", id);
            throw new IllegalStateException(
                    "No se puede desactivar al empleado porque tiene subordinados activos. Reasigne el equipo primero."
            );
        }

        empleado.setActivo(false);

        log.info("Empleado id={} desactivado correctamente", id);
    }

    private Empleado findEmpleadoById(Integer id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Empleado no encontrado con id={}", id);
                    return new ResourceNotFoundException("Empleado no encontrado con el id: " + id);
                });
    }
}