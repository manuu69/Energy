package org.example.energy.controller;

import org.example.energy.dto.error.ErrorResponseDTO;
import org.example.energy.dto.factura.FacturaResponseDTO;
import org.example.energy.exception.code.ErrorCode;
import org.example.energy.exception.handler.GlobalExceptionHandler;
import org.example.energy.exception.type.ResourceNotFoundException;
import org.example.energy.mapper.ErrorMapper;
import org.example.energy.mapper.ErrorMapperImpl;
import org.example.energy.service.FacturaService;
import org.example.energy.testutil.FacturaTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacturaController.class)
@Import({GlobalExceptionHandler.class})
public class FacturaControllerTest {

    private final static String API_URL = "/api/v1/facturas/";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ErrorMapper errorMapper;

    @MockitoBean
    private FacturaService facturaService;

    @Test
    void getById_whenFacturaExists_returns200() throws Exception {
        FacturaResponseDTO dto = FacturaTestData.crearFacturaResponseDTO();

        when(facturaService.getById(1)).thenReturn(dto);

        mockMvc.perform(get(API_URL + "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facturaId").value(1));

        verify(facturaService).getById(1);
    }

    @Test
    void getById_WhenFacturaNotExists_returns404() throws Exception {
        // Debes mantener el thenThrow para simular la excepción de negocio
        when(facturaService.getById(999))
                .thenThrow(new ResourceNotFoundException("Factura no encontrada"));

        mockMvc.perform(get(API_URL + "999"))
                .andExpect(status().isNotFound());

        verify(facturaService).getById(999);
    }

    @Test
    void getById_WhenIdIsNotNumeric_returns400() throws Exception {
        when(errorMapper.toErrorResponseDTO(any(ErrorCode.class), anyString(), anyString()))
                .thenReturn(new ErrorResponseDTO(
                        java.time.LocalDateTime.now(),
                        400,
                        "ERR_002",
                        "Bad Request",
                        "El parámetro de la ruta no coincide con el tipo esperado",
                        API_URL + "abv",
                        null
                ));

        mockMvc.perform(get(API_URL + "abv"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(facturaService);
    }
}