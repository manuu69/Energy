package org.example.energy.dashboard.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dashboard.dto.ResumenFacturacionClienteResponseDTO;
import org.example.energy.dashboard.service.ResumenFacturacionClienteService;
import org.example.energy.dashboard.entity.ResumenFacturacionClienteView;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.dashboard.mapper.ResumenFacturacionClienteMapper;
import org.example.energy.dashboard.repository.ResumenFacturacionClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ResumenFacturacionClienteServiceImpl implements ResumenFacturacionClienteService {

    private final ResumenFacturacionClienteRepository repository;
    private final ResumenFacturacionClienteMapper mapper;

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<ResumenFacturacionClienteResponseDTO> getAll(Pageable pageable) {
        Page<ResumenFacturacionClienteView> resumenes = repository.findAll(pageable);
        return resumenes.map(mapper::toDTO);
    }

    /**
     * @param clienteId
     * @return
     */
    @Override
    public ResumenFacturacionClienteResponseDTO getByClienteId(Integer clienteId) {
        ResumenFacturacionClienteView resumen = repository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con el id: " + clienteId));
        return mapper.toDTO(resumen);
    }
}
