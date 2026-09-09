package org.example.energy.service;

import org.example.energy.common.enums.EstadoIncidencia;
import org.example.energy.common.enums.TipoIncidencia;
import org.example.energy.common.exception.type.BusinessRuleException;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.contrato.entity.Contrato;
import org.example.energy.contrato.repository.ContratoRepository;
import org.example.energy.incidencia.dto.IncidenciaCreateDTO;
import org.example.energy.incidencia.dto.IncidenciaResponseDTO;
import org.example.energy.incidencia.dto.IncidenciaUpdateDTO;
import org.example.energy.incidencia.entity.Incidencia;
import org.example.energy.incidencia.mapper.IncidenciaMapper;
import org.example.energy.incidencia.repository.IncidenciaCriticaRepository;
import org.example.energy.incidencia.repository.IncidenciaRepository;
import org.example.energy.incidencia.service.IncidenciaServiceImpl;
import org.example.energy.testUtil.IncidenciaTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidenciaServiceImplTest {

    @Mock
    private IncidenciaRepository incidenciaRepository;

    @Mock
    private IncidenciaCriticaRepository incidenciaCriticaRepository;

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private IncidenciaMapper incidenciaMapper;

    @InjectMocks
    private IncidenciaServiceImpl incidenciaService;

    @Test
    void getAll_DebeRetornarPaginaDeIncidencias() {
        Pageable pageable = PageRequest.of(0, 10);
        Incidencia incidencia = IncidenciaTestData.crearIncidencia();
        IncidenciaResponseDTO responseDTO = IncidenciaTestData.crearIncidenciaResponseDTO();
        Page<Incidencia> page = new PageImpl<>(List.of(incidencia));

        when(incidenciaRepository.findAll(pageable)).thenReturn(page);
        when(incidenciaMapper.toDTO(incidencia)).thenReturn(responseDTO);

        Page<IncidenciaResponseDTO> result = incidenciaService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(incidenciaRepository).findAll(pageable);
    }

    @Test
    void getById_CuandoExiste_DebeRetornarIncidenciaDTO() {
        Incidencia incidencia = IncidenciaTestData.crearIncidencia();
        IncidenciaResponseDTO responseDTO = IncidenciaTestData.crearIncidenciaResponseDTO();

        when(incidenciaRepository.findById(1)).thenReturn(Optional.of(incidencia));
        when(incidenciaMapper.toDTO(incidencia)).thenReturn(responseDTO);

        IncidenciaResponseDTO result = incidenciaService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.incidenciaId());
        verify(incidenciaRepository).findById(1);
    }

    @Test
    void getById_CuandoNoExiste_DebeLanzarResourceNotFoundException() {
        when(incidenciaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> incidenciaService.getById(99));
        verify(incidenciaRepository).findById(99);
    }

    @Test
    void create_Exitoso_DebeGuardarYRetornarDTO() {
        IncidenciaCreateDTO createDTO = IncidenciaTestData.crearIncidenciaCreateDTO();
        Contrato contrato = new Contrato();
        contrato.setContratoId(10);
        Incidencia incidencia = IncidenciaTestData.crearIncidencia();
        IncidenciaResponseDTO responseDTO = IncidenciaTestData.crearIncidenciaResponseDTO();

        when(contratoRepository.findById(10)).thenReturn(Optional.of(contrato));
        when(incidenciaMapper.toEntity(createDTO)).thenReturn(incidencia);
        when(incidenciaRepository.save(any(Incidencia.class))).thenReturn(incidencia);
        when(incidenciaMapper.toDTO(incidencia)).thenReturn(responseDTO);

        IncidenciaResponseDTO result = incidenciaService.create(createDTO);

        assertNotNull(result);
        verify(contratoRepository).findById(10);
        verify(incidenciaRepository).save(incidencia);
    }

    @Test
    void create_CuandoContratoNoExiste_DebeLanzarResourceNotFoundException() {
        IncidenciaCreateDTO createDTO = IncidenciaTestData.crearIncidenciaCreateDTO();

        when(contratoRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> incidenciaService.create(createDTO));
        verify(contratoRepository).findById(10);
        verifyNoInteractions(incidenciaRepository);
    }

    @Test
    void update_Exitoso_DebeActualizarYRetornarDTO() {
        IncidenciaUpdateDTO updateDTO = IncidenciaTestData.crearIncidenciaUpdateDTO();
        Incidencia incidencia = IncidenciaTestData.crearIncidencia();
        IncidenciaResponseDTO responseDTO = IncidenciaTestData.crearIncidenciaResponseDTO();

        when(incidenciaRepository.findById(1)).thenReturn(Optional.of(incidencia));
        when(incidenciaMapper.toDTO(incidencia)).thenReturn(responseDTO);

        IncidenciaResponseDTO result = incidenciaService.update(1, updateDTO);

        assertNotNull(result);
        verify(incidenciaMapper).updateEntityFromDTO(updateDTO, incidencia);
        verify(incidenciaRepository).findById(1);
    }

    @Test
    void iniciarGestion_CuandoEstadoEsAbierta_DebeCambiarAEnGestion() {
        Incidencia incidencia = IncidenciaTestData.crearIncidencia();
        IncidenciaResponseDTO responseDTO = IncidenciaTestData.crearIncidenciaResponseDTO();

        when(incidenciaRepository.findById(1)).thenReturn(Optional.of(incidencia));
        when(incidenciaMapper.toDTO(incidencia)).thenReturn(responseDTO);

        IncidenciaResponseDTO result = incidenciaService.iniciarGestion(1);

        assertNotNull(result);
        assertEquals(EstadoIncidencia.EN_GESTION, incidencia.getEstado());
        verify(incidenciaRepository).findById(1);
    }

    @Test
    void iniciarGestion_CuandoYaEstaEnGestion_DebeLanzarBusinessRuleException() {
        Incidencia incidencia = IncidenciaTestData.crearIncidenciaEnGestion();

        when(incidenciaRepository.findById(1)).thenReturn(Optional.of(incidencia));

        assertThrows(BusinessRuleException.class, () -> incidenciaService.iniciarGestion(1));
        verify(incidenciaRepository).findById(1);
    }

    @Test
    void iniciarGestion_CuandoEstaCerrada_DebeLanzarBusinessRuleException() {
        Incidencia incidencia = IncidenciaTestData.crearIncidenciaCerrada();

        when(incidenciaRepository.findById(1)).thenReturn(Optional.of(incidencia));

        assertThrows(BusinessRuleException.class, () -> incidenciaService.iniciarGestion(1));
        verify(incidenciaRepository).findById(1);
    }

    @Test
    void cerrar_CuandoEstaAbiertaOEnGestion_DebeCerrar() {
        Incidencia incidencia = IncidenciaTestData.crearIncidencia();
        IncidenciaResponseDTO responseDTO = IncidenciaTestData.crearIncidenciaResponseDTO();

        when(incidenciaRepository.findById(1)).thenReturn(Optional.of(incidencia));
        when(incidenciaMapper.toDTO(incidencia)).thenReturn(responseDTO);

        IncidenciaResponseDTO result = incidenciaService.cerrar(1);

        assertNotNull(result);
        assertEquals(EstadoIncidencia.CERRADA, incidencia.getEstado());
        assertNotNull(incidencia.getFechaCierre());
        verify(incidenciaRepository).findById(1);
    }

    @Test
    void cerrar_CuandoYaEstaCerrada_DebeLanzarBusinessRuleException() {
        Incidencia incidencia = IncidenciaTestData.crearIncidenciaCerrada();

        when(incidenciaRepository.findById(1)).thenReturn(Optional.of(incidencia));

        assertThrows(BusinessRuleException.class, () -> incidenciaService.cerrar(1));
        verify(incidenciaRepository).findById(1);
    }

    /*@Test
    void getIncidenciasCriticasByContrato_CuandoExisteContrato_DebeRetornarLista() {
        IncidenciaCritica critica = IncidenciaTestData.crearIncidenciaCritica();
        IncidenciaCriticaDTO criticaDTO = IncidenciaTestData.crearIncidenciaCriticaDTO();

        when(contratoRepository.existsById(10)).thenReturn(true);
        when(incidenciaCriticaRepository.findByContratoId(10)).thenReturn(List.of(critica));
        when(incidenciaMapper.toCriticaDTO(critica)).thenReturn(criticaDTO);

        List<IncidenciaCriticaDTO> result = incidenciaService.getIncidenciasCriticasByContrato(10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(contratoRepository).existsById(10);
        verify(incidenciaCriticaRepository).findByContratoId(10);
    }*/

    @Test
    void getIncidenciasCriticasByContrato_CuandoNoExisteContrato_DebeLanzarExcepcion() {
        when(contratoRepository.existsById(99)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> incidenciaService.getIncidenciasCriticasByContrato(99));
        verify(contratoRepository).existsById(99);
        verifyNoInteractions(incidenciaCriticaRepository);
    }
}