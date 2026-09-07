package org.example.energy.dashboard.service;

import org.example.energy.common.enums.TipoCliente;
import org.example.energy.dashboard.dto.*;

import java.util.List;

public interface DashboardResumenService {
    DashboardResumenDTO getResumen();
    List<TopDeudorDTO> getDeudores(int limit);
    FacturacionMensualDTO getFacturacionMensual(int mes);
    Long countByTipoCliente(TipoCliente tipoCliente);
    List<IncidenciaPorTipoDTO> getIncidenciaByTipo();
    List<ConsumoPorZonaDTO> getConsumoPorZona();
}
