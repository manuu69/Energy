package org.example.energy.controller;

import org.example.energy.dto.factura.FacturaCreateDTO;
import org.example.energy.dto.factura.FacturaResponseDTO;
import org.example.energy.exception.code.ErrorCode;
import org.example.energy.exception.handler.GlobalExceptionHandler;
import org.example.energy.exception.type.BusinessRuleException;
import org.example.energy.exception.type.ResourceNotFoundException;
import org.example.energy.mapper.ErrorMapper;
import org.example.energy.service.FacturaService;
import org.example.energy.testutil.FacturaTestData;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacturaController.class)
@Import({GlobalExceptionHandler.class})
public class FacturaControllerTest {

    private final static String API_URL = "/api/v1/facturas";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ErrorMapper errorMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FacturaService facturaService;

    @Test
    void getById_whenFacturaExists_returns200() throws Exception {
        FacturaResponseDTO dto = FacturaTestData.crearFacturaResponseDTO();

        when(facturaService.getById(1)).thenReturn(dto);

        mockMvc.perform(get(API_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facturaId").value(1));

        verify(facturaService).getById(1);
    }

    @Test
    void getById_WhenFacturaNotExists_returns404() throws Exception {
        // Debes mantener el thenThrow para simular la excepción de negocio
        when(facturaService.getById(999))
                .thenThrow(new ResourceNotFoundException("Factura no encontrada"));

        mockMvc.perform(get(API_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(facturaService).getById(999);
    }

    @Test
    void getById_WhenIdIsNotNumeric_returns400() throws Exception {
        mockMvc.perform(get(API_URL + "/abv"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(facturaService);
    }

    @Test
    void getAll_returns200WithPage() throws Exception {
        Page<FacturaResponseDTO> page = new PageImpl<>(
                List.of(FacturaTestData.crearFacturaResponseDTO())
        );

        when(facturaService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath(".content[0].facturaId").value(1));

        verify(facturaService).getAll(any(Pageable.class));
    }

    @Test
    void getAll_whenEmpty_returns200WithEmptyPage() throws Exception {
        when(facturaService.getAll(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(facturaService).getAll(any(Pageable.class));
    }

    @Test
    void getByContratoId_whenContratoExists_returns200() throws Exception {
        List<FacturaResponseDTO> lista = List.of(FacturaTestData.crearFacturaResponseDTO());

        when(facturaService.getByContratoId(1)).thenReturn(lista);

        mockMvc.perform(get(API_URL + "/contrato/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].facturaId").value(1));

        verify(facturaService).getByContratoId(1);
    }

    @Test
    void getByContratoId_whenContratoNotExists_returns200() throws Exception {
        when(facturaService.getByContratoId(999))
                .thenThrow(new ResourceNotFoundException("Contrato no encontrado"));

        mockMvc.perform(get(API_URL + "/contrato/999"))
                .andExpect(status().isNotFound());

        verify(facturaService).getByContratoId(999);
    }


    //POST
    @Test
    void create_whenValidDTO_returns201() throws Exception {
        FacturaCreateDTO createDTO = FacturaTestData.crearFacturaCreateDTO();
        FacturaResponseDTO responseDTO = FacturaTestData.crearFacturaResponseDTO();
        when(facturaService.create(any(FacturaCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.facturaId").value(1));

        verify(facturaService).create(any(FacturaCreateDTO.class));
    }

    @Test
    void create_whenInvalidDTO_returns201() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(facturaService);
    }

    @Test
    void create_whenBodyIsMissing_returns400() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(facturaService);
    }

    //PATCH PAGAR_FACTURAS
    @Test
    void pagarFactura_whenFacturaExists_returns200() throws Exception {
        FacturaResponseDTO dto = FacturaTestData.crearFacturaResponseDTOPagada();
        when(facturaService.pagarFactura(1)).thenReturn(dto);

        mockMvc.perform(patch(API_URL + "/1/pagar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facturaId").value(1));

        verify(facturaService).pagarFactura(1);
    }

    @Test
    void pagarFactura_whenFacturaNotExists_returns404() throws Exception {
        when(facturaService.pagarFactura(999)).thenThrow(
                new ResourceNotFoundException("Factura no encontrada")
        );

        mockMvc.perform(patch(API_URL + "/999/pagar"))
                .andExpect(status().isNotFound());

        verify(facturaService).pagarFactura(999);
    }

    @Test
    void pagarFactura_whenFacturaYaPagada_returns409() throws Exception {
        when(facturaService.pagarFactura(1)).thenThrow(
                new BusinessRuleException(ErrorCode.FACTURA_YA_PAGADA)
        );

        mockMvc.perform(patch(API_URL + "/1/pagar"))
                .andExpect(status().isConflict());

        verify(facturaService).pagarFactura(1);
    }

    //POST CANCELAR PAGO
    @Test
    void cancelarFactura_whenFacturaExists_returns200() throws Exception {
        FacturaResponseDTO dto = FacturaTestData.crearFacturaResponseDTOCancelada();
        when(facturaService.cancelarFactura(1)).thenReturn(dto);

        mockMvc.perform(patch(API_URL + "/1/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facturaId").value(1));

        verify(facturaService).cancelarFactura(1);
    }

    @Test
    void cancelarFactura_whenFacturaNotExists_returns404() throws Exception {
        when(facturaService.cancelarFactura(999)).thenThrow(
                new ResourceNotFoundException("Factura no encontrada")
        );

        mockMvc.perform(patch(API_URL + "/999/cancelar"))
                .andExpect(status().isNotFound());

        verify(facturaService).cancelarFactura(999);
    }

    @Test
    void cancelarFactura_whenFacturaYaCancelada_returns409() throws Exception {
        when(facturaService.cancelarFactura(999)).thenThrow(
                new BusinessRuleException(ErrorCode.FACTURA_YA_CANCELADA)
        );

        mockMvc.perform(patch(API_URL + "/999/cancelar"))
                .andExpect(status().isConflict());

        verify(facturaService).cancelarFactura(999);
    }

    //POST GENERAR FACTURAS MES
    @Test
    void generarFacturas_whenMesValido_returns204() throws Exception {
        doNothing().when(facturaService).generarFacturas(1);

        mockMvc.perform(post(API_URL + "/generar/1"))
                .andExpect(status().isNoContent());
        verify(facturaService).generarFacturas(1);
    }

    @Test
    void generarFacturas_whenMesInvalido_returns204() throws Exception {
        mockMvc.perform(post(API_URL + "/generar/abv"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(facturaService);
    }

    //DELETE
    @Test
    void deleteById_whenFacturaExists_returns204() throws Exception {
        doNothing().when(facturaService).deleteById(1);

        mockMvc.perform(delete(API_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(facturaService).deleteById(1);
    }

    @Test
    void deleteById_whenFacturaNotExists_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("Factura no encontrada"))
                .when(facturaService).deleteById(999);

        mockMvc.perform(delete(API_URL + "/999"))
                .andExpect(status().isNotFound());

        verify(facturaService).deleteById(999);
    }

    @Test
    void deleteById_whenFacturaPagada_returns409() throws Exception {
        doThrow(new BusinessRuleException(ErrorCode.FACTURA_YA_PAGADA))
                .when(facturaService).deleteById(1);

        mockMvc.perform(delete(API_URL + "/1"))
                .andExpect(status().isConflict());

        verify(facturaService).deleteById(1);
    }
}