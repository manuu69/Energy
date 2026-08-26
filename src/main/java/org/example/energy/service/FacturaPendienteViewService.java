package org.example.energy.service;

import org.example.energy.dto.factura.FacturaPendienteResponseDTO;
import org.example.energy.dto.factura.FacturaResponseDTO;

import java.util.List;

public interface FacturaPendienteViewService {
    List<FacturaResponseDTO> getFacturaPendientes();
}
