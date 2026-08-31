package org.example.energy.service;

import org.example.energy.dto.lectura.LecturaCreateDTO;
import org.example.energy.dto.lectura.LecturaResponseDTO;
import org.example.energy.dto.lectura.LecturaUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LecturaService {
    Page<LecturaResponseDTO> getAll(Pageable pageable);
    LecturaResponseDTO getById(Integer id);
    Page<LecturaResponseDTO> getByContratoId(Integer id);
    LecturaResponseDTO create(LecturaCreateDTO dto);
    LecturaResponseDTO update(LecturaUpdateDTO dto);
    void delete(Integer id);

}
