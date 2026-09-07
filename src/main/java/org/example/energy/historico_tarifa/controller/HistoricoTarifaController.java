package org.example.energy.historico_tarifa.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.energy.historico_tarifa.dto.HistoricoTarifaResponseDTO;
import org.example.energy.historico_tarifa.service.HistoricoTarifaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/historico-tarifas")
@RequiredArgsConstructor
@Tag(name = "Historico de cambios de tarifas")
public class HistoricoTarifaController {

    private final HistoricoTarifaService historicoTarifaService;

    @GetMapping
    public ResponseEntity<List<HistoricoTarifaResponseDTO>> getByContratoId(@RequestParam Integer contratoId){
        return ResponseEntity.ok().body(historicoTarifaService.getByContratoId(contratoId));
    }
}
