package org.example.energy.service.serviceImpl;

import lombok.AllArgsConstructor;
import org.example.energy.dto.FacturaCreateDTO;
import org.example.energy.dto.FacturaResponseDTO;
import org.example.energy.entity.domain.Factura;
import org.example.energy.exception.BusinessRuleException;
import org.example.energy.exception.ErrorCode;
import org.example.energy.exception.ResourceNotFoundException;
import org.example.energy.mapper.FacturaMapper;
import org.example.energy.repository.domain.FacturaRepository;
import org.example.energy.service.FacturaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;
    private final FacturaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<FacturaResponseDTO> getAll(Pageable pageable) {
        Page<Factura> facturas = facturaRepository.findAll(pageable);
        return facturas.map(mapper::toDTO);
    }

    @Override
    public FacturaResponseDTO getById(Integer id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Factura no encontrada con el ID: " + id
                ));
        return mapper.toDTO(factura);
    }

    @Override
    public List<FacturaResponseDTO> getByContratoId(Integer id) {
        return mapper.toDTOList(facturaRepository.findByContratoContratoId(id));
    }

    @Override
    public FacturaResponseDTO create(FacturaCreateDTO dto) {
        return null;
    }

    @Override
    public FacturaResponseDTO pagarFactura(Integer id) {
        return null;
    }

    @Override
    public FacturaResponseDTO cancelarFactura(Integer id) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }
}
