package org.example.energy.service;

import org.example.energy.dto.contrato.ContratoCreateDTO;
import org.example.energy.dto.contrato.ContratoResponseDTO;
import org.example.energy.dto.contrato.ContratoUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContratoService {
    Page<ContratoResponseDTO> getAll(Pageable pageable);
    ContratoResponseDTO getById(Integer id);
    ContratoResponseDTO create(ContratoCreateDTO dto);
    ContratoResponseDTO update(Integer id, ContratoUpdateDTO dto);
    ContratoResponseDTO cancelar(Integer id);
    ContratoResponseDTO activar(Integer id);
    void deleteById(Integer id);
}
