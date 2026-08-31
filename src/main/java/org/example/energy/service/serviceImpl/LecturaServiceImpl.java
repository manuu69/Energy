package org.example.energy.service.serviceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.lectura.LecturaCreateDTO;
import org.example.energy.dto.lectura.LecturaResponseDTO;
import org.example.energy.dto.lectura.LecturaUpdateDTO;
import org.example.energy.entity.domain.Lectura;
import org.example.energy.exception.type.ResourceNotFoundException;
import org.example.energy.mapper.LecturaMapper;
import org.example.energy.repository.domain.LecturaRepository;
import org.example.energy.service.LecturaService;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Slf4j
@AllArgsConstructor
@Service
public class LecturaServiceImpl implements LecturaService {

    private final LecturaRepository lecturaRepository;
    private final LecturaMapper lecturaMapper;


    @Override
    public Page<LecturaResponseDTO> getAll(Pageable pageable) {
        Page<Lectura> lecturas = lecturaRepository.findAll(pageable);

        return lecturas.map(lecturaMapper::toDTO);
    }

    @Override
    public LecturaResponseDTO getById(Integer id) {
        Lectura lectura = findLecturaById(id);

        return lecturaMapper.toDTO(lectura);
    }

    @Override
    public Page<LecturaResponseDTO> getByContratoId(Integer id) {
        return null;
    }

    @Override
    public LecturaResponseDTO create(LecturaCreateDTO dto) {
        return null;
    }

    @Override
    public LecturaResponseDTO update(LecturaUpdateDTO dto) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

    private @NonNull Lectura findLecturaById(Integer id){
        return lecturaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Lectura no encontrada con id={}", id);

                    return new ResourceNotFoundException(
                            "Lectura no encontrada con el ID: " + id
                    );
                });
    }
}
