package org.example.energy.controller;

import org.example.energy.factura.controller.FacturaPendienteViewController;
import org.example.energy.factura.dto.FacturaResponseDTO;
import org.example.energy.common.error.mapper.ErrorMapper;
import org.example.energy.factura.service.FacturaPendienteViewService;
import org.example.energy.testutil.FacturaTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacturaPendienteViewController.class)
public class FacturaPendienteViewControlloerTest {

    private final static String API_URL = "/api/v1/facturas/pendientes";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ErrorMapper errorMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FacturaPendienteViewService facturaPendienteService;

    @Test
    void getAll_whenFacturasPendientesExist_returns200() throws Exception {
        FacturaResponseDTO dto =
                FacturaTestData.crearFacturaResponseDTOPendiente();

        when(facturaPendienteService.getFacturaPendientes())
                .thenReturn(List.of(dto));

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].facturaId").value(dto.facturaId()));

        verify(facturaPendienteService).getFacturaPendientes();
    }
}
