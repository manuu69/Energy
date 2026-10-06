package org.example.energy.dashboard.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.common.enums.TipoCliente;
import org.example.energy.dashboard.dto.*;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.dashboard.repository.DashboardResumenViewRepository;
import org.example.energy.dashboard.service.DashboardResumenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DashboardResumenServiceImpl implements DashboardResumenService {

    private final DashboardResumenViewRepository dashboardRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResumenDTO getResumen() {
        log.debug("Consultando datos generales del dashboard");

        DashboardResumenDTO resumen = dashboardRepository.getResumen()
                .orElseThrow(() -> {
                    log.warn("No se encontraron datos para generar el dashboard");
                    return new ResourceNotFoundException("No hay ningun dashboard");
                });

        log.info("Datos generales del dashboard obtenidos correctamente");
        return resumen;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopDeudorDTO> getDeudores(int limit) {
        log.debug("Consultando top deudores con limit={}", limit);

        List<TopDeudorDTO> deudores = dashboardRepository.getTopDeudor(limit);

        log.info("Consulta de top deudores realizada. Resultados encontrados: total={}", deudores.size());
        return deudores;
    }

    @Override
    @Transactional(readOnly = true)
    public FacturacionMensualDTO getFacturacionMensual(int mes) {
        log.debug("Consultando facturación mensual para mes={}", mes);

        FacturacionMensualDTO facturacion = dashboardRepository.getFacturacionMensual(mes)
                .orElseThrow(() -> {
                    log.warn("No se encontró facturación mensual para el mes={}", mes);
                    return new ResourceNotFoundException("Ninguna factura encontrada para el mes: " + mes);
                });

        log.info("Facturación mensual obtenida correctamente para mes={}", mes);
        return facturacion;
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByTipoCliente(TipoCliente tipoCliente) {
        log.debug("Contando clientes por tipoCliente={}", tipoCliente);

        Long total = dashboardRepository.countClienteByTipo(tipoCliente);

        log.info("Conteo de clientes por tipoCliente={} realizado. Total={}", tipoCliente, total);
        return total;
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidenciaPorTipoDTO> getIncidenciaByTipo() {
        log.debug("Consultando incidencias agrupadas por tipo");

        List<IncidenciaPorTipoDTO> incidencias = dashboardRepository.getByTipoIncidencia();

        log.info("Consulta de incidencias por tipo realizada. Registros obtenidos={}", incidencias.size());
        return incidencias;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsumoPorZonaDTO> getConsumoPorZona() {
        log.debug("Consultando consumos agrupados por zona");

        List<ConsumoPorZonaDTO> consumos = dashboardRepository.getConsumoPorZona();

        log.info("Consulta de consumo por zona realizada. Zonas obtenidas={}", consumos.size());
        return consumos;
    }
}