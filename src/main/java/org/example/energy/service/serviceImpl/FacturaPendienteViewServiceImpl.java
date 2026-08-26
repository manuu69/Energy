package org.example.energy.service.serviceImpl;

import lombok.AllArgsConstructor;
import org.example.energy.dto.factura.FacturaResponseDTO;
import org.example.energy.mapper.FacturaPendienteMapper;
import org.example.energy.repository.view.FacturaPendienteViewRepository;
import org.example.energy.service.FacturaPendienteViewService;
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
