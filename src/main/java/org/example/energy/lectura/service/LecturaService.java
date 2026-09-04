package org.example.energy.lectura.service;

import org.example.energy.lectura.dto.LecturaAnalisisDTO;
import org.example.energy.lectura.dto.LecturaCreateDTO;
import org.example.energy.lectura.dto.LecturaResponseDTO;
import org.example.energy.lectura.dto.LecturaUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface LecturaService {
    Page<LecturaResponseDTO> getAll(Pageable pageable);
    LecturaResponseDTO getById(Integer id);
    Page<LecturaResponseDTO> getByContratoId(Integer id, Pageable pageable);
    LecturaResponseDTO create(LecturaCreateDTO dto);
    LecturaResponseDTO update(LecturaUpdateDTO dto, Integer id);
    void delete(Integer id);


    List<LecturaAnalisisDTO> getAnalisis();
    List<LecturaAnalisisDTO> getAnalisisByContrato(Integer contratoId);
    List<LecturaAnalisisDTO> getAnomalias(BigDecimal umbral);
}
