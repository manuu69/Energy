package org.example.energy.service.serviceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.ClienteCreateDTO;
import org.example.energy.dto.ClienteResponseDTO;
import org.example.energy.dto.ClienteUpdateDTO;
import org.example.energy.mapper.ClienteMapper;
import org.example.energy.repository.domain.ClienteRepository;
import org.example.energy.service.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ClienteServiceImpl implements ClienteService {


    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public Page<ClienteResponseDTO> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public ClienteResponseDTO getById(Integer id) {
        return null;
    }

    @Override
    public ClienteResponseDTO getByEmail(String email) {
        return null;
    }

    @Override
    public ClienteResponseDTO getByCiudad(String ciudad) {
        return null;
    }

    @Override
    public ClienteResponseDTO getBySegmento(String segmento) {
        return null;
    }

    @Override
    public ClienteResponseDTO getByTipo(String tipo) {
        return null;
    }

    @Override
    public ClienteResponseDTO create(ClienteCreateDTO dto) {
        return null;
    }

    @Override
    public ClienteResponseDTO update(ClienteUpdateDTO dto, Integer id) {
        return null;
    }

    @Override
    public void darBaja(Integer id) {

    }
}
