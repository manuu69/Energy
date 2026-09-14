package org.example.energy.controller;

import org.example.energy.cliente.controller.ClienteController;
import org.example.energy.cliente.dto.ClienteCreateDTO;
import org.example.energy.cliente.dto.ClienteResponseDTO;
import org.example.energy.cliente.dto.ClienteUpdateDTO;
import org.example.energy.cliente.service.ClienteService;
import org.example.energy.common.error.mapper.ErrorMapper;
import org.example.energy.common.exception.code.ErrorCode;
import org.example.energy.common.exception.handler.GlobalExceptionHandler;
import org.example.energy.common.exception.type.BusinessRuleException;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.testUtil.ClienteTestData;
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

@WebMvcTest(ClienteController.class)
@Import({GlobalExceptionHandler.class})
public class ClienteControllerTest {

    private final static String API_URL = "/clientes";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ErrorMapper errorMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteService clienteService;

    // GET BY ID
    @Test
    void getById_whenClienteExists_returns200() throws Exception {
        ClienteResponseDTO dto = ClienteTestData.crearClienteResponseDTO();

        when(clienteService.getById(1)).thenReturn(dto);

        mockMvc.perform(get(API_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(1));

        verify(clienteService).getById(1);
    }

    @Test
    void getById_whenClienteNotExists_returns404() throws Exception {
        when(clienteService.getById(999))
                .thenThrow(new ResourceNotFoundException("Cliente no encontrado"));

        mockMvc.perform(get(API_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(clienteService).getById(999);
    }

    @Test
    void getById_whenIdIsNotNumeric_returns400() throws Exception {
        mockMvc.perform(get(API_URL + "/abv"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(clienteService);
    }

    // GET ALL
    @Test
    void getAll_returns200WithPage() throws Exception {
        Page<ClienteResponseDTO> page = new PageImpl<>(
                List.of(ClienteTestData.crearClienteResponseDTO())
        );

        when(clienteService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].clienteId").value(1));

        verify(clienteService).getAll(any(Pageable.class));
    }

    @Test
    void getAll_whenEmpty_returns200WithEmptyPage() throws Exception {
        when(clienteService.getAll(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(clienteService).getAll(any(Pageable.class));
    }

    // POST
    @Test
    void create_whenValidDTO_returns201() throws Exception {
        ClienteCreateDTO createDTO = ClienteTestData.crearClienteCreateDTO();
        ClienteResponseDTO responseDTO = ClienteTestData.crearClienteResponseDTO();

        when(clienteService.create(any(ClienteCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId").value(1));

        verify(clienteService).create(any(ClienteCreateDTO.class));
    }

    @Test
    void create_whenInvalidDTO_returns400() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(clienteService);
    }

    @Test
    void create_whenBodyIsMissing_returns400() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(clienteService);
    }

    // PUT
    @Test
    void update_whenClienteExists_returns200() throws Exception {
        ClienteUpdateDTO updateDTO = ClienteTestData.crearClienteUpdateDTO();
        ClienteResponseDTO responseDTO = ClienteTestData.crearClienteResponseDTO();

        when(clienteService.update(eq(1), any(ClienteUpdateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put(API_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(1));

        verify(clienteService).update(eq(1), any(ClienteUpdateDTO.class));
    }

    @Test
    void update_whenClienteNotExists_returns404() throws Exception {
        ClienteUpdateDTO updateDTO = ClienteTestData.crearClienteUpdateDTO();

        when(clienteService.update(eq(999), any(ClienteUpdateDTO.class)))
                .thenThrow(new ResourceNotFoundException("Cliente no encontrado"));

        mockMvc.perform(put(API_URL + "/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());

        verify(clienteService).update(eq(999), any(ClienteUpdateDTO.class));
    }

    // DELETE
    @Test
    void deleteById_whenClienteExists_returns204() throws Exception {
        doNothing().when(clienteService).darBaja(1);

        mockMvc.perform(delete(API_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(clienteService).darBaja(1);
    }

    @Test
    void deleteById_whenClienteNotExists_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("Cliente no encontrado"))
                .when(clienteService).darBaja(999);

        mockMvc.perform(delete(API_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(clienteService).darBaja(999);
    }

    /*@Test
    void deleteById_whenClienteTieneContratosActivos_returns409() throws Exception {
        doThrow(new BusinessRuleException(ErrorCode.CLIENTE_))
                .when(clienteService).deleteById(1);

        mockMvc.perform(delete(API_URL + "/1"))
                .andExpect(status().isConflict());

        verify(clienteService).deleteById(1);
    }*/
}