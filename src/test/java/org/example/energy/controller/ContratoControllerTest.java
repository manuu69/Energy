package org.example.energy.controller;

import org.example.energy.common.error.mapper.ErrorMapper;
import org.example.energy.common.exception.code.ErrorCode;
import org.example.energy.common.exception.handler.GlobalExceptionHandler;
import org.example.energy.common.exception.type.BusinessRuleException;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.contrato.controller.ContratoController;
import org.example.energy.contrato.dto.ContratoCreateDTO;
import org.example.energy.contrato.dto.ContratoResponseDTO;
import org.example.energy.contrato.dto.ContratoUpdateDTO;
import org.example.energy.contrato.service.ContratoService;
import org.example.energy.testUtil.ContratoTestData;
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

@WebMvcTest(ContratoController.class)
@Import({GlobalExceptionHandler.class})
public class ContratoControllerTest {

    private final static String API_URL = "/contratos";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ErrorMapper errorMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ContratoService contratoService;

    // GET BY ID
    @Test
    void getById_whenContratoExists_returns200() throws Exception {
        ContratoResponseDTO dto = ContratoTestData.crearContratoResponseDTO();

        when(contratoService.getById(8)).thenReturn(dto);

        mockMvc.perform(get(API_URL + "/8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contratoId").value(8));

        verify(contratoService).getById(8);
    }

    @Test
    void getById_whenContratoNotExists_returns404() throws Exception {
        when(contratoService.getById(999))
                .thenThrow(new ResourceNotFoundException("Contrato no encontrado"));

        mockMvc.perform(get(API_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(contratoService).getById(999);
    }

    @Test
    void getById_whenIdIsNotNumeric_returns400() throws Exception {
        mockMvc.perform(get(API_URL + "/abv"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(contratoService);
    }

    // GET ALL
    @Test
    void getAll_returns200WithPage() throws Exception {
        Page<ContratoResponseDTO> page = new PageImpl<>(
                List.of(ContratoTestData.crearContratoResponseDTO())
        );

        when(contratoService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].contratoId").value(8));

        verify(contratoService).getAll(any(Pageable.class));
    }

    @Test
    void getAll_whenEmpty_returns200WithEmptyPage() throws Exception {
        when(contratoService.getAll(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(contratoService).getAll(any(Pageable.class));
    }

    // POST
    @Test
    void create_whenValidDTO_returns201() throws Exception {
        ContratoCreateDTO createDTO = ContratoTestData.crearContratoCreateDTO();
        ContratoResponseDTO responseDTO = ContratoTestData.crearContratoResponseDTO();

        when(contratoService.create(any(ContratoCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contratoId").value(8));

        verify(contratoService).create(any(ContratoCreateDTO.class));
    }

    @Test
    void create_whenInvalidDTO_returns400() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(contratoService);
    }

    @Test
    void create_whenBodyIsMissing_returns400() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(contratoService);
    }

    // PUT
    @Test
    void update_whenContratoExists_returns200() throws Exception {
        ContratoUpdateDTO updateDTO = ContratoTestData.crearContratoUpdateDTO();
        ContratoResponseDTO responseDTO = ContratoTestData.crearContratoResponseDTO();

        when(contratoService.update(eq(8), any(ContratoUpdateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put(API_URL + "/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contratoId").value(8));

        verify(contratoService).update(eq(8), any(ContratoUpdateDTO.class));
    }

    @Test
    void update_whenContratoNotExists_returns404() throws Exception {
        ContratoUpdateDTO updateDTO = ContratoTestData.crearContratoUpdateDTO();

        when(contratoService.update(eq(999), any(ContratoUpdateDTO.class)))
                .thenThrow(new ResourceNotFoundException("Contrato no encontrado"));

        mockMvc.perform(put(API_URL + "/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());

        verify(contratoService).update(eq(999), any(ContratoUpdateDTO.class));
    }

    // PATCH /cancelar
    @Test
    void cancelarContrato_whenContratoExists_returns200() throws Exception {
        ContratoResponseDTO dto = ContratoTestData.crearContratoResponseDTO();

        when(contratoService.suspender(8)).thenReturn(dto);

        mockMvc.perform(patch(API_URL + "/8/suspension"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contratoId").value(8));

        verify(contratoService).suspender(8);
    }

    @Test
    void cancelarContrato_whenContratoNotExists_returns404() throws Exception {
        when(contratoService.suspender(999))
                .thenThrow(new ResourceNotFoundException("Contrato no encontrado"));

        mockMvc.perform(patch(API_URL + "/999/suspension"))
                .andExpect(status().isNotFound());

        verify(contratoService).suspender(999);
    }

    @Test
    void cancelarContrato_whenContratoYaCancelado_returns409() throws Exception {
        when(contratoService.suspender(1))
                .thenThrow(new BusinessRuleException(ErrorCode.CONTRATO_YA_SUSPENDIDO));

        mockMvc.perform(patch(API_URL + "/1/suspension"))
                .andExpect(status().isConflict());

        verify(contratoService).suspender(1);
    }

    // DELETE
    @Test
    void deleteById_whenContratoExists_returns204() throws Exception {
        doNothing().when(contratoService).deleteById(1);

        mockMvc.perform(delete(API_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(contratoService).deleteById(1);
    }

    @Test
    void deleteById_whenContratoNotExists_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("Contrato no encontrado"))
                .when(contratoService).deleteById(999);

        mockMvc.perform(delete(API_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(contratoService).deleteById(999);
    }
}