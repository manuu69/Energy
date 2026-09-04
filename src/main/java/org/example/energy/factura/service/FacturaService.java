package org.example.energy.factura.service;

import org.example.energy.factura.dto.FacturaCreateDTO;
import org.example.energy.factura.dto.FacturaResponseDTO;
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
    int actualizarFacturasVencidas();
}
