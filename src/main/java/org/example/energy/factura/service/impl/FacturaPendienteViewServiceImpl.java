package org.example.energy.factura.service.impl;

import lombok.AllArgsConstructor;
import org.example.energy.factura.dto.FacturaResponseDTO;
import org.example.energy.factura.mapper.FacturaPendienteMapper;
import org.example.energy.factura.repository.FacturaPendienteViewRepository;
import org.example.energy.factura.service.FacturaPendienteViewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class FacturaPendienteViewServiceImpl implements FacturaPendienteViewService {

    private final FacturaPendienteViewRepository repository;
    private final FacturaPendienteMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> getFacturaPendientes() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }
}
