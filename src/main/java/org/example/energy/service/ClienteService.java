package org.example.energy.service;

import org.example.energy.dto.ClienteCreateDTO;
import org.example.energy.dto.ClienteResponseDTO;
import org.example.energy.dto.ClienteUpdateDTO;
import org.example.energy.dto.FacturaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {
    Page<ClienteResponseDTO> getAll(Pageable pageable);
    ClienteResponseDTO getById(Integer id);
    ClienteResponseDTO getByEmail(String email);
    ClienteResponseDTO getByCiudad(String ciudad);
    ClienteResponseDTO getBySegmento(String segmento);
    ClienteResponseDTO getByTipo(String tipo);
    ClienteResponseDTO create(ClienteCreateDTO dto);
    ClienteResponseDTO update(ClienteUpdateDTO dto, Integer id);
    void darBaja(Integer id);
}
