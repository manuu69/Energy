package org.example.energy.service;

import org.example.energy.dto.factura.FacturaCreateDTO;
import org.example.energy.dto.factura.FacturaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FacturaService {
    Page<FacturaResponseDTO> getAll(Pageable pageable);
    FacturaResponseDTO getById(Integer id);
    List<FacturaResponseDTO> getByContratoId(Integer id);
    FacturaResponseDTO create(FacturaCreateDTO dto);
    FacturaResponseDTO pagarFactura(Integer id);
    FacturaResponseDTO cancelarFactura(Integer id);
    void generarFacturas(Integer mes);
    void deleteById(Integer id);
}
