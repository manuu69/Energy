package org.example.energy.service;

import org.example.energy.dto.zona.ZonaResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZonaService {
    List<ZonaResponseDTO> findAll();
    List<ZonaResponseDTO> findSubzonas();
    List<ZonaResponseDTO> getZonaCompleta();
    List<ZonaResponseDTO> getClientes();
    ZonaResponseDTO findById(Integer id);
}
