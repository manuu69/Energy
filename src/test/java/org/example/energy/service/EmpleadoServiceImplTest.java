package org.example.energy.empleado.service;

import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.empleado.dto.EmpleadoCreateDTO;
import org.example.energy.empleado.dto.EmpleadoResponseDTO;
import org.example.energy.empleado.dto.EmpleadoUpdateDTO;
import org.example.energy.empleado.entity.Empleado;
import org.example.energy.empleado.mapper.EmpleadoMapper;
import org.example.energy.empleado.repository.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.energy.testUtil.EmpleadoTestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceImplTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private EmpleadoMapper empleadoMapper;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    @Test
    void create_whenValidDataWithoutJefe_shouldReturnEmpleadoResponseDTO() {
        EmpleadoCreateDTO createDTO = crearEmpleadoCreateDTO(null);
        Empleado empleado = crearEmpleado();
        empleado.setJefe(null);
        EmpleadoResponseDTO responseDTO = crearEmpleadoResponseDTO();

        when(empleadoMapper.toEntity(createDTO)).thenReturn(empleado);
        when(empleadoRepository.save(empleado)).thenReturn(empleado);
        when(empleadoMapper.toDTO(empleado)).thenReturn(responseDTO);

        EmpleadoResponseDTO result = empleadoService.create(createDTO);

        assertThat(result).isNotNull();
        assertThat(result.empleadoId()).isEqualTo(responseDTO.empleadoId());

        verify(empleadoRepository, never()).findById(anyInt());
        verify(empleadoRepository).save(empleado);
        verify(empleadoMapper).toDTO(empleado);
    }

    @Test
    void create_whenJefeExists_shouldAssignJefeAndReturnDTO() {
        Integer jefeId = 1;
        EmpleadoCreateDTO createDTO = crearEmpleadoCreateDTO(jefeId);
        Empleado jefe = crearJefe();
        Empleado empleado = crearEmpleado();
        EmpleadoResponseDTO responseDTO = crearEmpleadoResponseDTO();

        when(empleadoMapper.toEntity(createDTO)).thenReturn(empleado);
        when(empleadoRepository.findById(jefeId)).thenReturn(Optional.of(jefe));
        when(empleadoRepository.save(empleado)).thenReturn(empleado);
        when(empleadoMapper.toDTO(empleado)).thenReturn(responseDTO);

        EmpleadoResponseDTO result = empleadoService.create(createDTO);

        assertThat(result).isNotNull();
        assertThat(empleado.getJefe()).isEqualTo(jefe);

        verify(empleadoRepository).findById(jefeId);
        verify(empleadoRepository).save(empleado);
    }

    @Test
    void create_whenJefeDoesNotExist_shouldThrowResourceNotFoundException() {
        Integer jefeId = 999;
        EmpleadoCreateDTO createDTO = crearEmpleadoCreateDTO(jefeId);
        Empleado empleado = crearEmpleado();

        when(empleadoMapper.toEntity(createDTO)).thenReturn(empleado);
        when(empleadoRepository.findById(jefeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> empleadoService.create(createDTO))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(empleadoRepository).findById(jefeId);
        verify(empleadoRepository, never()).save(any());
    }

    @Test
    void update_whenEmpleadoExists_shouldUpdateAndReturnDTO() {
        Integer empleadoId = 2;
        Empleado empleado = crearEmpleado();
        EmpleadoUpdateDTO updateDTO = crearEmpleadoUpdateDTO();
        EmpleadoResponseDTO responseDTO = crearEmpleadoResponseDTO();

        when(empleadoRepository.findById(empleadoId)).thenReturn(Optional.of(empleado));
        doNothing().when(empleadoMapper).updateEntityFromDto(updateDTO, empleado);
        //when(empleadoRepository.save(empleado)).thenReturn(empleado);
        when(empleadoMapper.toDTO(empleado)).thenReturn(responseDTO);

        EmpleadoResponseDTO result = empleadoService.update(empleadoId, updateDTO);

        assertThat(result).isNotNull();
        verify(empleadoRepository).findById(empleadoId);
        verify(empleadoMapper).updateEntityFromDto(updateDTO, empleado);
        //verify(empleadoRepository).save(empleado);
    }

    @Test
    void delete_whenNoSubordinadosActivos_shouldSetActivoFalse() {
        Integer empleadoId = 2;
        Empleado empleado = crearEmpleado();

        when(empleadoRepository.findById(empleadoId)).thenReturn(Optional.of(empleado));
        when(empleadoRepository.existsByJefeEmpleadoIdAndActivoTrue(empleadoId)).thenReturn(false);

        empleadoService.delete(empleadoId);

        assertThat(empleado.getActivo()).isFalse();

        verify(empleadoRepository).findById(empleadoId);
        verify(empleadoRepository).existsByJefeEmpleadoIdAndActivoTrue(empleadoId);
    }

    @Test
    void delete_whenHasSubordinadosActivos_shouldThrowIllegalStateException() {
        Integer jefeId = 1;
        Empleado jefe = crearJefe();

        when(empleadoRepository.findById(jefeId)).thenReturn(Optional.of(jefe));
        when(empleadoRepository.existsByJefeEmpleadoIdAndActivoTrue(jefeId)).thenReturn(true);

        assertThatThrownBy(() -> empleadoService.delete(jefeId))
                .isInstanceOf(IllegalStateException.class);

        assertThat(jefe.getActivo()).isTrue();

        verify(empleadoRepository).findById(jefeId);
        verify(empleadoRepository).existsByJefeEmpleadoIdAndActivoTrue(jefeId);
    }
}