package org.example.energy.historico_tarifa.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.historico_tarifa.dto.HistoricoTarifaResponseDTO;
import org.example.energy.historico_tarifa.repository.HistoricoTarifaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class HistoricoTarifaServiceImpl implements HistoricoTarifaService{

    private final HistoricoTarifaRepository historicoTarifaRepository;


    /**
     * @param contratoId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<HistoricoTarifaResponseDTO> getByContratoId(Integer contratoId) {
        return historicoTarifaRepository.findByContratoId(contratoId);
    }
}
