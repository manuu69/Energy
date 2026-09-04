package org.example.energy.zona.service;

import org.example.energy.zona.dto.ZonaResponseDTO;

import java.util.List;

public interface ZonaService {
    List<ZonaResponseDTO> findAll();
    List<ZonaResponseDTO> findSubzonas(Integer id);
    List<ZonaResponseDTO> getZonaCompleta();
    List<ZonaResponseDTO> getClientes();
    ZonaResponseDTO findById(Integer id);
}
