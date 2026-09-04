package org.example.energy.contrato.service;

import org.example.energy.contrato.dto.ContratoCreateDTO;
import org.example.energy.contrato.dto.ContratoResponseDTO;
import org.example.energy.contrato.dto.ContratoUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContratoService {
    Page<ContratoResponseDTO> getAll(Pageable pageable);
    ContratoResponseDTO getById(Integer id);
    ContratoResponseDTO create(ContratoCreateDTO dto);
    ContratoResponseDTO update(Integer id, ContratoUpdateDTO dto);
    ContratoResponseDTO darBaja(Integer id);
    ContratoResponseDTO suspender(Integer id);
    ContratoResponseDTO activar(Integer id);
    void deleteById(Integer id);
}
