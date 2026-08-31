package org.example.energy.service.serviceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.zona.ZonaResponseDTO;
import org.example.energy.entity.domain.Contrato;
import org.example.energy.entity.domain.Zona;
import org.example.energy.exception.type.ResourceNotFoundException;
import org.example.energy.mapper.ZonaMapper;
import org.example.energy.repository.domain.ZonaRepository;
import org.example.energy.service.ZonaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ZonaServicempl implements ZonaService {

    private final ZonaRepository zonaRepository;
    private final ZonaMapper zonaMapper;

    @Override
    public List<ZonaResponseDTO> findAll() {
        return zonaRepository.findAll().stream().map(zonaMapper::toResponseDTO).toList();
    }

    @Override
    public List<ZonaResponseDTO> findSubzonas(Integer id) {
        if (!zonaRepository.existsById(id)){
            throw new ResourceNotFoundException("La zona no existe con el id: " + id);
        }
        return zonaRepository.findById(id).stream().map(zonaMapper::toResponseDTO).toList();
    }

    @Override
    public List<ZonaResponseDTO> getZonaCompleta() {
        return List.of();
    }

    @Override
    public List<ZonaResponseDTO> getClientes() {
        return List.of();
    }

    @Override
    public ZonaResponseDTO findById(Integer id) {
        return zonaMapper.toResponseDTO(findZonaById(id));
    }


    private Zona findZonaById(Integer id){
        return zonaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Zona no encontrada con id={}", id);

                    return new ResourceNotFoundException(
                            "Zona no encontrada con el ID: " + id
                    );
                });
    }
}
