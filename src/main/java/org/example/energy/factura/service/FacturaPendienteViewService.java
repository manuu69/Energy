package org.example.energy.factura.service;

import org.example.energy.factura.dto.FacturaResponseDTO;

import java.util.List;

public interface FacturaPendienteViewService {
    List<FacturaResponseDTO> getFacturaPendientes();
}
