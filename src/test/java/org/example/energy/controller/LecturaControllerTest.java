package org.example.energy.controller;

import org.example.energy.common.error.mapper.ErrorMapper;
import org.example.energy.common.exception.code.ErrorCode;
import org.example.energy.common.exception.handler.GlobalExceptionHandler;
import org.example.energy.common.exception.type.BusinessRuleException;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.lectura.controller.LecturaController;
import org.example.energy.lectura.dto.LecturaCreateDTO;
import org.example.energy.lectura.dto.LecturaResponseDTO;
import org.example.energy.lectura.service.LecturaService;
import org.example.energy.testUtil.LecturaTestData;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LecturaController.class)
@Import({GlobalExceptionHandler.class})
public class LecturaControllerTest {

    private final static String API_URL = "/api/v1/lecturas";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ErrorMapper errorMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LecturaService lecturaService;

    // GET BY ID
    @Test
    void getById_whenLecturaExists_returns200() throws Exception {
        LecturaResponseDTO dto = LecturaTestData.crearLecturaResponseDTO();

        when(lecturaService.getById(1)).thenReturn(dto);

        mockMvc.perform(get(API_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lecturaId").value(1));

        verify(lecturaService).getById(1);
    }

    @Test
    void getById_whenLecturaNotExists_returns404() throws Exception {
        when(lecturaService.getById(999))
                .thenThrow(new ResourceNotFoundException("Lectura no encontrada"));

        mockMvc.perform(get(API_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(lecturaService).getById(999);
    }

    @Test
    void getById_whenIdIsNotNumeric_returns400() throws Exception {
        mockMvc.perform(get(API_URL + "/abv"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(lecturaService);
    }

    // GET ALL
    @Test
    void getAll_returns200WithPage() throws Exception {
        Page<LecturaResponseDTO> page = new PageImpl<>(
                List.of(LecturaTestData.crearLecturaResponseDTO())
        );

        when(lecturaService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].lecturaId").value(1));

        verify(lecturaService).getAll(any(Pageable.class));
    }

    @Test
    void getAll_whenEmpty_returns200WithEmptyPage() throws Exception {
        when(lecturaService.getAll(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(lecturaService).getAll(any(Pageable.class));
    }

    // GET BY CONTRATO ID
    @Test
    void getByContratoId_whenContratoExists_returns200() throws Exception {
        Page<LecturaResponseDTO> page = new PageImpl<>(
                List.of(LecturaTestData.crearLecturaResponseDTO())
        );

        when(lecturaService.getByContratoId(eq(1), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(API_URL + "/contrato/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].lecturaId").value(1));

        verify(lecturaService).getByContratoId(eq(1), any(Pageable.class));
    }

    @Test
    void getByContratoId_whenContratoNotExists_returns404() throws Exception {
        Page<LecturaResponseDTO> page = new PageImpl<>(
                List.of(LecturaTestData.crearLecturaResponseDTO())
        );

        when(lecturaService.getByContratoId(eq(999), any(Pageable.class)))
                .thenThrow(new ResourceNotFoundException("Contrato no encontrado"));

        mockMvc.perform(get(API_URL + "/contrato/999"))
                .andExpect(status().isNotFound());

        verify(lecturaService).getByContratoId(eq(999), any(Pageable.class));
    }

    // POST
    @Test
    void create_whenValidDTO_returns201() throws Exception {
        LecturaCreateDTO createDTO = LecturaTestData.crearLecturaCreateDTO();
        LecturaResponseDTO responseDTO = LecturaTestData.crearLecturaResponseDTO();

        when(lecturaService.create(any(LecturaCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lecturaId").value(1));

        verify(lecturaService).create(any(LecturaCreateDTO.class));
    }

    @Test
    void create_whenInvalidDTO_returns400() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(lecturaService);
    }

    @Test
    void create_whenBodyIsMissing_returns400() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(lecturaService);
    }

    /*@Test
    void create_whenLecturaAnteriorSuperior_returns409() throws Exception {
        LecturaCreateDTO createDTO = LecturaTestData.crearLecturaCreateDTO();

        when(lecturaService.create(any(LecturaCreateDTO.class)))
                .thenThrow(new BusinessRuleException(ErrorCode.LECTURA_INVALIDA));

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isConflict());

        verify(lecturaService).create(any(LecturaCreateDTO.class));
    }*/

    // DELETE
    @Test
    void deleteById_whenLecturaExists_returns204() throws Exception {
        doNothing().when(lecturaService).delete(1);

        mockMvc.perform(delete(API_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(lecturaService).delete(1);
    }

    @Test
    void deleteById_whenLecturaNotExists_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("Lectura no encontrada"))
                .when(lecturaService).delete(999);

        mockMvc.perform(delete(API_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(lecturaService).delete(999);
    }

    /*@Test
    void deleteById_whenLecturaYaFacturada_returns409() throws Exception {
        doThrow(new BusinessRuleException(ErrorCode.))
                .when(lecturaService).deleteById(1);

        mockMvc.perform(delete(API_URL + "/1"))
                .andExpect(status().isConflict());

        verify(lecturaService).delete(1);
    }*/
}