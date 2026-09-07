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
    //private final DashboardResumenViewMapper dashboardMapper;

    /**
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public DashboardResumenDTO getResumen() {
        return dashboardRepository.getResumen()
                .orElseThrow(() -> new ResourceNotFoundException("No hay nigun dashboard"));
    }


    @Override
    @Transactional(readOnly = true)
    public List<TopDeudorDTO> getDeudores(int limit) {
        return dashboardRepository.getTopDeudor(limit);
    }

    /**
     * @param mes
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public FacturacionMensualDTO getFacturacionMensual(int mes) {
        return dashboardRepository.getFacturacionMensual(mes)
                .orElseThrow(() -> new ResourceNotFoundException("Ninguna factura encontrada para el mes: " + mes));
    }

    /**
     * @param tipoCliente
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Long countByTipoCliente(TipoCliente tipoCliente) {
        return dashboardRepository.countClienteByTipo(tipoCliente);
    }

    /**
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<IncidenciaPorTipoDTO> getIncidenciaByTipo() {
        return dashboardRepository.getByTipoIncidencia();
    }

    /**
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<ConsumoPorZonaDTO> getConsumoPorZona() {
        return dashboardRepository.getConsumoPorZona();
    }
}
