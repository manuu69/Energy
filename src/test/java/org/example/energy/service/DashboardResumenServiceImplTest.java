package org.example.energy.service;

import org.example.energy.common.enums.TipoCliente;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.dashboard.dto.*;
import org.example.energy.dashboard.repository.DashboardResumenViewRepository;
import org.example.energy.dashboard.service.impl.DashboardResumenServiceImpl;
import org.example.energy.testUtil.DashboardTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardResumenServiceImplTest {

    @Mock
    private DashboardResumenViewRepository dashboardRepository;

    @InjectMocks
    private DashboardResumenServiceImpl dashboardResumenService;

    @Test
    void getResumen_CuandoExiste_DebeRetornarDashboardResumenDTO() {
        DashboardResumenDTO resumenDTO = DashboardTestData.crearDashboardResumenDTO();

        when(dashboardRepository.getResumen()).thenReturn(Optional.of(resumenDTO));

        DashboardResumenDTO result = dashboardResumenService.getResumen();

        assertNotNull(result);
        assertEquals(150L, result.clientesActivos());
        assertEquals(new BigDecimal("4500.50"), result.importePendiente());
        verify(dashboardRepository).getResumen();
    }

    @Test
    void getResumen_CuandoNoExiste_DebeLanzarResourceNotFoundException() {
        when(dashboardRepository.getResumen()).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dashboardResumenService.getResumen());
        verify(dashboardRepository).getResumen();
    }

    @Test
    void getDeudores_DebeRetornarListaDeTopDeudores() {
        int limit = 5;
        TopDeudorDTO deudorDTO = DashboardTestData.crearTopDeudorDTO();

        when(dashboardRepository.getTopDeudor(limit)).thenReturn(List.of(deudorDTO));

        List<TopDeudorDTO> result = dashboardResumenService.getDeudores(limit);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Empresa Ficticia S.L.", result.get(0).nombre());
        verify(dashboardRepository).getTopDeudor(limit);
    }

    @Test
    void getFacturacionMensual_CuandoExiste_DebeRetornarFacturacionMensualDTO() {
        int mes = 5;
        FacturacionMensualDTO facturacionDTO = DashboardTestData.crearFacturacionMensualDTO();

        when(dashboardRepository.getFacturacionMensual(mes)).thenReturn(Optional.of(facturacionDTO));

        FacturacionMensualDTO result = dashboardResumenService.getFacturacionMensual(mes);

        assertNotNull(result);
        assertEquals(new BigDecimal("15000.00"), result.totalFacturado());
        verify(dashboardRepository).getFacturacionMensual(mes);
    }

    @Test
    void getFacturacionMensual_CuandoNoExiste_DebeLanzarResourceNotFoundException() {
        int mes = 13;

        when(dashboardRepository.getFacturacionMensual(mes)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dashboardResumenService.getFacturacionMensual(mes));
        verify(dashboardRepository).getFacturacionMensual(mes);
    }

    @Test
    void countByTipoCliente_DebeRetornarCantidad() {
        TipoCliente tipoCliente = TipoCliente.RESIDENCIAL;

        when(dashboardRepository.countClienteByTipo(tipoCliente)).thenReturn(42L);

        Long result = dashboardResumenService.countByTipoCliente(tipoCliente);

        assertNotNull(result);
        assertEquals(42L, result);
        verify(dashboardRepository).countClienteByTipo(tipoCliente);
    }

    @Test
    void getIncidenciaByTipo_DebeRetornarListaDeIncidencias() {
        IncidenciaPorTipoDTO dto = DashboardTestData.crearIncidenciaPorTipoDTO();

        when(dashboardRepository.getByTipoIncidencia()).thenReturn(List.of(dto));

        List<IncidenciaPorTipoDTO> result = dashboardResumenService.getIncidenciaByTipo();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("AVERIA", result.get(0).tipo());
        verify(dashboardRepository).getByTipoIncidencia();
    }

    @Test
    void getConsumoPorZona_DebeRetornarListaDeConsumos() {
        ConsumoPorZonaDTO dto = DashboardTestData.crearConsumoPorZonaDTO();

        when(dashboardRepository.getConsumoPorZona()).thenReturn(List.of(dto));

        List<ConsumoPorZonaDTO> result = dashboardResumenService.getConsumoPorZona();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Zona Centro", result.get(0).zona());
        verify(dashboardRepository).getConsumoPorZona();
    }
}