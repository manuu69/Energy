package org.example.energy.controller;

import org.example.energy.common.error.mapper.ErrorMapper;
import org.example.energy.common.exception.handler.GlobalExceptionHandler;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.dashboard.controller.ResumenFacturacionClienteController;
import org.example.energy.dashboard.dto.ResumenFacturacionClienteResponseDTO;
import org.example.energy.dashboard.service.ResumenFacturacionClienteService;
import org.example.energy.testUtil.ResumenFacturacionClienteTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ResumenFacturacionClienteController.class)
@Import({GlobalExceptionHandler.class})
public class ResumenFacturacionClienteControllerTest {

    private final static String API_URL = "/resumenes/facturacion-clientes";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ErrorMapper errorMapper;

    @MockitoBean
    private ResumenFacturacionClienteService resumenService;

    // GET ALL
    @Test
    void getAll_returns200WithPage() throws Exception {
        Page<ResumenFacturacionClienteResponseDTO> page = new PageImpl<>(
                List.of(ResumenFacturacionClienteTestData.crearResumenFacturacionClienteResponseDTO())
        );

        when(resumenService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0]").exists());

        verify(resumenService).getAll(any(Pageable.class));
    }

    // GET BY CLIENTE ID
    @Test
    void getByClienteId_whenClienteExists_returns200() throws Exception {
        ResumenFacturacionClienteResponseDTO dto = ResumenFacturacionClienteTestData.crearResumenFacturacionClienteResponseDTO();

        when(resumenService.getByClienteId(1)).thenReturn(dto);

        mockMvc.perform(get(API_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(1));

        verify(resumenService).getByClienteId(1);
    }

    @Test
    void getByClienteId_whenClienteNotExists_returns404() throws Exception {
        when(resumenService.getByClienteId(999))
                .thenThrow(new ResourceNotFoundException("No se encontró resumen para el cliente con ID: 999"));

        mockMvc.perform(get(API_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(resumenService).getByClienteId(999);
    }

    @Test
    void getByClienteId_whenIdIsNotNumeric_returns400() throws Exception {
        mockMvc.perform(get(API_URL + "/abc"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(resumenService);
    }
}