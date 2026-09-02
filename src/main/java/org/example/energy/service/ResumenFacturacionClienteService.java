package org.example.energy.service;

import org.example.energy.dto.dashboard.resumen.ResumenFacturacionClienteResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResumenFacturacionClienteService {

    Page<ResumenFacturacionClienteResponseDTO> getAll(Pageable pageable);

    ResumenFacturacionClienteResponseDTO getByClienteId(Integer clienteId);
}