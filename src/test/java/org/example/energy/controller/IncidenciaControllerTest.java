package org.example.energy.controller;

import org.example.energy.common.enums.EstadoIncidencia;
import org.example.energy.common.error.mapper.ErrorMapper;
import org.example.energy.common.exception.code.ErrorCode;
import org.example.energy.common.exception.handler.GlobalExceptionHandler;
import org.example.energy.common.exception.type.BusinessRuleException;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.incidencia.controller.IncidenciaController;
import org.example.energy.incidencia.dto.IncidenciaCreateDTO;
import org.example.energy.incidencia.dto.IncidenciaResponseDTO;
import org.example.energy.incidencia.dto.IncidenciaUpdateDTO;
import org.example.energy.incidencia.service.IncidenciaService;
import org.example.energy.testUtil.IncidenciaTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncidenciaController.class)
@Import({GlobalExceptionHandler.class})
public class IncidenciaControllerTest {

    private final static String API_URL = "/incidencias";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ErrorMapper errorMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IncidenciaService incidenciaService;

    // GET BY ID
    @Test
    void getById_whenIncidenciaExists_returns200() throws Exception {
        IncidenciaResponseDTO dto = IncidenciaTestData.crearIncidenciaResponseDTO();

        when(incidenciaService.getById(1)).thenReturn(dto);

        mockMvc.perform(get(API_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incidenciaId").value(1));

        verify(incidenciaService).getById(1);
    }

    @Test
    void getById_whenIncidenciaNotExists_returns404() throws Exception {
        when(incidenciaService.getById(999))
                .thenThrow(new ResourceNotFoundException("Incidencia no encontrada"));

        mockMvc.perform(get(API_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(incidenciaService).getById(999);
    }

    @Test
    void getById_whenIdIsNotNumeric_returns400() throws Exception {
        mockMvc.perform(get(API_URL + "/abv"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(incidenciaService);
    }

    // GET ALL
    @Test
    void getAll_returns200WithPage() throws Exception {
        Page<IncidenciaResponseDTO> page = new PageImpl<>(
                List.of(IncidenciaTestData.crearIncidenciaResponseDTO())
        );

        when(incidenciaService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].incidenciaId").value(1));

        verify(incidenciaService).getAll(any(Pageable.class));
    }

    @Test
    void getAll_whenEmpty_returns200WithEmptyPage() throws Exception {
        when(incidenciaService.getAll(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(incidenciaService).getAll(any(Pageable.class));
    }

    // GET BY CONTRATO ID (PAGINADO)
    @Test
    void getByContratoId_whenContratoExists_returns200() throws Exception {
        Page<IncidenciaResponseDTO> page = new PageImpl<>(
                List.of(IncidenciaTestData.crearIncidenciaResponseDTO())
        );

        when(incidenciaService.getByContratoId(eq(1), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(API_URL + "/contrato/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].incidenciaId").value(1));

        verify(incidenciaService).getByContratoId(eq(1), any(Pageable.class));
    }

    @Test
    void getByContratoId_whenContratoNotExists_returns404() throws Exception {
        when(incidenciaService.getByContratoId(eq(999), any(Pageable.class)))
                .thenThrow(new ResourceNotFoundException("Contrato no encontrado"));

        mockMvc.perform(get(API_URL + "/contrato/999"))
                .andExpect(status().isNotFound());

        verify(incidenciaService).getByContratoId(eq(999), any(Pageable.class));
    }

    // POST
    @Test
    void create_whenValidDTO_returns201() throws Exception {
        IncidenciaCreateDTO createDTO = IncidenciaTestData.crearIncidenciaCreateDTO();
        IncidenciaResponseDTO responseDTO = IncidenciaTestData.crearIncidenciaResponseDTO();

        when(incidenciaService.create(any(IncidenciaCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.incidenciaId").value(1));

        verify(incidenciaService).create(any(IncidenciaCreateDTO.class));
    }

    @Test
    void create_whenInvalidDTO_returns400() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(incidenciaService);
    }

    @Test
    void create_whenBodyIsMissing_returns400() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(incidenciaService);
    }

    // PUT / UPDATE
    @Test
    void update_whenIncidenciaExists_returns200() throws Exception {
        IncidenciaUpdateDTO updateDTO = IncidenciaTestData.crearIncidenciaUpdateDTO();
        IncidenciaResponseDTO responseDTO = IncidenciaTestData.crearIncidenciaResponseDTO();

        when(incidenciaService.update(eq(1), any(IncidenciaUpdateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put(API_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incidenciaId").value(1));

        verify(incidenciaService).update(eq(1), any(IncidenciaUpdateDTO.class));
    }

    @Test
    void update_whenIncidenciaNotExists_returns404() throws Exception {
        IncidenciaUpdateDTO updateDTO = IncidenciaTestData.crearIncidenciaUpdateDTO();

        when(incidenciaService.update(eq(999), any(IncidenciaUpdateDTO.class)))
                .thenThrow(new ResourceNotFoundException("Incidencia no encontrada"));

        mockMvc.perform(put(API_URL + "/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());

        verify(incidenciaService).update(eq(999), any(IncidenciaUpdateDTO.class));
    }

    // PATCH / RESOLVER
    @Test
    void resolverIncidencia_whenIncidenciaAbierta_returns200() throws Exception {
        IncidenciaResponseDTO dto = IncidenciaTestData.crearIncidenciaConEstadoDTO(EstadoIncidencia.CERRADA);

        when(incidenciaService.cerrar(1)).thenReturn(dto);

        mockMvc.perform(patch(API_URL + "/1/cerrar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incidenciaId").value(1));

        verify(incidenciaService).cerrar(1);
    }

    @Test
    void resolverIncidencia_whenIncidenciaYaCerrada_returns409() throws Exception {
        when(incidenciaService.cerrar(1))
                .thenThrow(new BusinessRuleException(ErrorCode.INCIDENCIA_YA_CERRADA));

        mockMvc.perform(patch(API_URL + "/1/cerrar"))
                .andExpect(status().isConflict());

        verify(incidenciaService).cerrar(1);
    }

    // DELETE
    /*@Test
    void deleteById_whenIncidenciaExists_returns204() throws Exception {
        doNothing().when(incidenciaService).cerrar(1);

        mockMvc.perform(delete(API_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(incidenciaService).cerrar(1);
    }

    @Test
    void deleteById_whenIncidenciaNotExists_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("Incidencia no encontrada"))
                .when(incidenciaService).cerrar(999);

        mockMvc.perform(delete(API_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(incidenciaService).cerrar(999);
    }*/
}