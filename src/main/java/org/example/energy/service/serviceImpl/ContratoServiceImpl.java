package org.example.energy.service.serviceImpl;

import org.example.energy.dto.contrato.ContratoCreateDTO;
import org.example.energy.dto.contrato.ContratoResponseDTO;
import org.example.energy.dto.contrato.ContratoUpdateDTO;
import org.example.energy.service.ContratoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ContratoServiceImpl implements ContratoService {
    @Override
    public Page<ContratoResponseDTO> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public ContratoResponseDTO getById(Integer id) {
        return null;
    }

    @Override
    public ContratoResponseDTO create(ContratoCreateDTO dto) {
        return null;
    }

    @Override
    public ContratoResponseDTO update(Integer id, ContratoUpdateDTO dto) {
        return null;
    }

    @Override
    public ContratoResponseDTO cancelar(Integer id) {
        return null;
    }

    @Override
    public ContratoResponseDTO activar(Integer id) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }
}
