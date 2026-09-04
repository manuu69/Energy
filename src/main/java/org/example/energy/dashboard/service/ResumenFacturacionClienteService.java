package org.example.energy.dashboard.service;

import org.example.energy.dashboard.dto.ResumenFacturacionClienteResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResumenFacturacionClienteService {

    Page<ResumenFacturacionClienteResponseDTO> getAll(Pageable pageable);

    ResumenFacturacionClienteResponseDTO getByClienteId(Integer clienteId);
}