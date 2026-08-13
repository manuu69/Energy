package org.example.energy.service;

import org.example.energy.dto.FacturaPendienteResponseDTO;

import java.util.List;

public interface FacturaPendienteViewService {
    List<FacturaPendienteResponseDTO> getFacturaPendientes();
}
