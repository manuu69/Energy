package org.example.energy.cliente.service;

import org.example.energy.cliente.dto.ClienteCreateDTO;
import org.example.energy.cliente.dto.ClienteResponseDTO;
import org.example.energy.cliente.dto.ClienteUpdateDTO;
import org.example.energy.common.enums.Segmento;
import org.example.energy.common.enums.TipoCliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {
    Page<ClienteResponseDTO> getAll(Pageable pageable);
    ClienteResponseDTO getById(Integer id);
    ClienteResponseDTO getByEmail(String email);
    Page<ClienteResponseDTO> getByCiudad(String ciudad, Pageable pageable);
    Page<ClienteResponseDTO> getBySegmento(Segmento segmento, Pageable pageable);
    Page<ClienteResponseDTO> getByTipo(TipoCliente tipo, Pageable pageable);
    ClienteResponseDTO create(ClienteCreateDTO dto);
    ClienteResponseDTO update(ClienteUpdateDTO dto, Integer id);
    void darBaja(Integer id);
}
