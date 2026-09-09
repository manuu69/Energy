package org.example.energy.service;

import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.contrato.repository.ContratoRepository;
import org.example.energy.lectura.dto.LecturaAnalisisDTO;
import org.example.energy.lectura.dto.LecturaCreateDTO;
import org.example.energy.lectura.dto.LecturaResponseDTO;
import org.example.energy.lectura.dto.LecturaUpdateDTO;
import org.example.energy.lectura.entity.Lectura;
import org.example.energy.lectura.entity.LecturaAnalisis;
import org.example.energy.lectura.mapper.LecturaMapper;
import org.example.energy.lectura.repository.LecturaAnalisisRepository;
import org.example.energy.lectura.repository.LecturaRepository;
import org.example.energy.lectura.service.LecturaServiceImpl;
import org.example.energy.testUtil.LecturaTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LecturaServiceImplTest {

    @Mock
    private LecturaRepository lecturaRepository;

    @Mock
    private LecturaAnalisisRepository lecturaAnalisisRepository;

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private LecturaMapper lecturaMapper;

    @InjectMocks
    private LecturaServiceImpl lecturaService;

    @Test
    void getAll_DebeRetornarPaginaDeLecturas() {
        Pageable pageable = PageRequest.of(0, 10);
        Lectura lectura = LecturaTestData.crearLectura();
        LecturaResponseDTO responseDTO = LecturaTestData.crearLecturaResponseDTO();
        Page<Lectura> lecturaPage = new PageImpl<>(List.of(lectura));

        when(lecturaRepository.findAll(pageable)).thenReturn(lecturaPage);
        when(lecturaMapper.toDTO(lectura)).thenReturn(responseDTO);

        Page<LecturaResponseDTO> result = lecturaService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(lecturaRepository).findAll(pageable);
    }

    @Test
    void getById_CuandoExiste_DebeRetornarLecturaDTO() {
        Lectura lectura = LecturaTestData.crearLectura();
        LecturaResponseDTO responseDTO = LecturaTestData.crearLecturaResponseDTO();

        when(lecturaRepository.findById(1)).thenReturn(Optional.of(lectura));
        when(lecturaMapper.toDTO(lectura)).thenReturn(responseDTO);

        LecturaResponseDTO result = lecturaService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.lecturaId());
        verify(lecturaRepository).findById(1);
    }

    @Test
    void getById_CuandoNoExiste_DebeLanzarResourceNotFoundException() {
        when(lecturaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> lecturaService.getById(99));
        verify(lecturaRepository).findById(99);
    }

    @Test
    void getByContratoId_CuandoExisteContrato_DebeRetornarPagina() {
        Pageable pageable = PageRequest.of(0, 10);
        Lectura lectura = LecturaTestData.crearLectura();
        LecturaResponseDTO responseDTO = LecturaTestData.crearLecturaResponseDTO();
        Page<Lectura> lecturaPage = new PageImpl<>(List.of(lectura));

        when(contratoRepository.existsById(10)).thenReturn(true);
        when(lecturaRepository.findByContratoContratoId(10, pageable)).thenReturn(lecturaPage);
        when(lecturaMapper.toDTO(lectura)).thenReturn(responseDTO);

        Page<LecturaResponseDTO> result = lecturaService.getByContratoId(10, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(contratoRepository).existsById(10);
    }

    @Test
    void getByContratoId_CuandoNoExisteContrato_DebeLanzarExcepcion() {
        Pageable pageable = PageRequest.of(0, 10);
        when(contratoRepository.existsById(99)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> lecturaService.getByContratoId(99, pageable));
        verify(contratoRepository).existsById(99);
        verifyNoInteractions(lecturaMapper);
    }

    @Test
    void create_Exitoso_DebeRegistrarYRetornarDTO() {
        LecturaCreateDTO createDTO = LecturaTestData.crearLecturaCreateDTO();
        Lectura lectura = LecturaTestData.crearLectura();
        LecturaResponseDTO responseDTO = LecturaTestData.crearLecturaResponseDTO();

        when(lecturaRepository.findLastLecturaByContratoId(createDTO.contratoId())).thenReturn(Optional.of(lectura));
        when(lecturaMapper.toDTO(lectura)).thenReturn(responseDTO);

        LecturaResponseDTO result = lecturaService.create(createDTO);

        assertNotNull(result);
        verify(lecturaRepository).registrarLectura(
                createDTO.contratoId(),
                createDTO.fecha(),
                createDTO.consumoKwh(),
                createDTO.tipoLectura().toString()
        );
        verify(lecturaRepository).findLastLecturaByContratoId(createDTO.contratoId());
    }

    @Test
    void update_Exitoso_DebeModificarYRetornarDTO() {
        LecturaUpdateDTO updateDTO = LecturaTestData.crearLecturaUpdateDTO();
        Lectura lectura = LecturaTestData.crearLectura();
        LecturaResponseDTO responseDTO = LecturaTestData.crearLecturaResponseDTO();

        when(lecturaRepository.findById(1)).thenReturn(Optional.of(lectura));
        when(lecturaMapper.toDTO(lectura)).thenReturn(responseDTO);

        LecturaResponseDTO result = lecturaService.update(updateDTO, 1);

        assertNotNull(result);
        verify(lecturaMapper).updateEntityFromDTO(updateDTO, lectura);
        verify(lecturaRepository).findById(1);
    }

    @Test
    void delete_CuandoExiste_DebeEliminar() {
        when(lecturaRepository.existsById(1)).thenReturn(true);

        lecturaService.delete(1);

        verify(lecturaRepository).existsById(1);
        verify(lecturaRepository).deleteById(1);
    }

    @Test
    void delete_CuandoNoExiste_DebeLanzarExcepcion() {
        when(lecturaRepository.existsById(99)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> lecturaService.delete(99));
        verify(lecturaRepository).existsById(99);
        verify(lecturaRepository, never()).deleteById(any());
    }
/*
    @Test
    void getAnalisis_DebeRetornarListaDTO() {
        LecturaAnalisis analisis = LecturaTestData.crearLecturaAnalisis();
        LecturaAnalisisDTO analisisDTO = LecturaTestData.crearLecturaAnalisisDTO();

        when(lecturaAnalisisRepository.findAll()).thenReturn(List.of(analisis));
        when(lecturaMapper.toAnalisisDTO(analisis)).thenReturn(analisisDTO);

        List<LecturaAnalisisDTO> result = lecturaService.getAnalisis();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(lecturaAnalisisRepository).findAll();
    }

    @Test
    void getAnomalias_ConUmbralNull_DebeUsarUmbralPorDefecto() {
        LecturaAnalisis analisis = LecturaTestData.crearLecturaAnalisis();
        LecturaAnalisisDTO analisisDTO = LecturaTestData.crearLecturaAnalisisDTO();

        when(lecturaAnalisisRepository.findAnomalias(BigDecimal.valueOf(50))).thenReturn(List.of(analisis));
        when(lecturaMapper.toAnalisisDTO(analisis)).thenReturn(analisisDTO);

        List<LecturaAnalisisDTO> result = lecturaService.getAnomalias(null);

        assertNotNull(result);
        verify(lecturaAnalisisRepository).findAnomalias(BigDecimal.valueOf(50));
    }*/
}