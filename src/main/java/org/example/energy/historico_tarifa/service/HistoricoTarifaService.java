package org.example.energy.historico_tarifa.service;

import org.example.energy.historico_tarifa.dto.HistoricoTarifaResponseDTO;

import java.util.List;

public interface HistoricoTarifaService {

    List<HistoricoTarifaResponseDTO> getByContratoId(Integer contratoId);
}
