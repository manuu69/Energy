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
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class ResumenFacturacionClienteServiceImpl implements ResumenFacturacionClienteService {

    private final ResumenFacturacionClienteRepository resumenRepository;

    /**
     * @param pageable
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ResumenFacturacionClienteResponseDTO> getAll(Pageable pageable) {
        return resumenRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenFacturacionClienteResponseDTO getByClienteId(Integer clienteId) {
        return resumenRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró resumen para el cliente con ID: " + clienteId));
    }
}
