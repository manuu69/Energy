package org.example.energy.controller;

import org.example.energy.common.error.mapper.ErrorMapper;
import org.example.energy.common.exception.handler.GlobalExceptionHandler;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.testUtil.ZonaTestData;
import org.example.energy.zona.controller.ZonaController;
import org.example.energy.zona.dto.ZonaResponseDTO;
import org.example.energy.zona.service.ZonaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ZonaController.class, properties = "spring.mvc.api-prefix=/api/v1")
@Import({GlobalExceptionHandler.class})
public class ZonaControllerTest {

    private final static String API_URL = "/zonas";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ErrorMapper errorMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private  ZonaService zonaService;

    // GET BY ID
    @Test
    void getById_whenZonaExists_returns200() throws Exception {
        ZonaResponseDTO dto = ZonaTestData.crearZonaResponseDTO();

        when(zonaService.findById(1)).thenReturn(dto);

        mockMvc.perform(get(API_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.zonaId").value(1));

        verify(zonaService).findById(1);
    }

    @Test
    void getById_whenZonaNotExists_returns404() throws Exception {
        when(zonaService.findById(999))
                .thenThrow(new ResourceNotFoundException("Zona no encontrada"));

        mockMvc.perform(get(API_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(zonaService).findById(999);
    }

    // GET ALL
    @Test
    void getAll_returns200WithList() throws Exception {
        List<ZonaResponseDTO> list = List.of(ZonaTestData.crearZonaResponseDTO());

        when(zonaService.findAll()).thenReturn(list);

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].zonaId").value(1));

        verify(zonaService).findAll();
    }


}