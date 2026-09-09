package org.example.energy.service;

import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.testUtil.ZonaTestData;
import org.example.energy.zona.dto.ZonaResponseDTO;
import org.example.energy.zona.entity.Zona;
import org.example.energy.zona.mapper.ZonaMapper;
import org.example.energy.zona.repository.ZonaRepository;
import org.example.energy.zona.service.ZonaServicempl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZonaServicemplTest {

    @Mock
    private ZonaRepository zonaRepository;

    @Mock
    private ZonaMapper zonaMapper;

    @InjectMocks
    private ZonaServicempl zonaService;

    @Test
    void findAll_DebeRetornarListaDeZonaResponseDTO() {
        Zona zona = ZonaTestData.crearZona();
        ZonaResponseDTO responseDTO = ZonaTestData.crearZonaResponseDTO();

        when(zonaRepository.findAll()).thenReturn(List.of(zona));
        when(zonaMapper.toResponseDTO(zona)).thenReturn(responseDTO);

        List<ZonaResponseDTO> result = zonaService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Zona Norte", result.get(0).nombre());
        verify(zonaRepository).findAll();
    }

    @Test
    void findById_CuandoExiste_DebeRetornarZonaDTO() {
        Zona zona = ZonaTestData.crearZona();
        ZonaResponseDTO responseDTO = ZonaTestData.crearZonaResponseDTO();

        when(zonaRepository.findById(1)).thenReturn(Optional.of(zona));
        when(zonaMapper.toResponseDTO(zona)).thenReturn(responseDTO);

        ZonaResponseDTO result = zonaService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.zonaId());
        verify(zonaRepository).findById(1);
    }

    @Test
    void findById_CuandoNoExiste_DebeLanzarResourceNotFoundException() {
        when(zonaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> zonaService.findById(99));
        verify(zonaRepository).findById(99);
    }

    @Test
    void findSubzonas_CuandoExisteZona_DebeRetornarLista() {
        Zona zona = ZonaTestData.crearZona();
        ZonaResponseDTO responseDTO = ZonaTestData.crearZonaResponseDTO();

        when(zonaRepository.existsById(1)).thenReturn(true);
        when(zonaRepository.findById(1)).thenReturn(Optional.of(zona));
        when(zonaMapper.toResponseDTO(zona)).thenReturn(responseDTO);

        List<ZonaResponseDTO> result = zonaService.findSubzonas(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(zonaRepository).existsById(1);
        verify(zonaRepository).findById(1);
    }

    @Test
    void findSubzonas_CuandoNoExisteZona_DebeLanzarExcepcion() {
        when(zonaRepository.existsById(99)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> zonaService.findSubzonas(99));
        verify(zonaRepository).existsById(99);
        verify(zonaRepository, never()).findById(any());
    }

    @Test
    void getZonaCompleta_DebeRetornarListaVacia() {
        List<ZonaResponseDTO> result = zonaService.getZonaCompleta();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getClientes_DebeRetornarListaVacia() {
        List<ZonaResponseDTO> result = zonaService.getClientes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}