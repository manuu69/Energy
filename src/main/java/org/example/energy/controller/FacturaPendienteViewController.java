package org.example.energy.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.energy.dto.FacturaPendienteResponseDTO;
import org.example.energy.service.serviceImpl.FacturaPendienteViewServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/facturas/pendientes")
@Tag(name = "Facturas pendientes")
public class FacturaPendienteViewController {

    private final FacturaPendienteViewServiceImpl facturaService;

    @GetMapping
    public ResponseEntity<List<FacturaPendienteResponseDTO>> getFacturasPendientes(){
        return ResponseEntity.ok(facturaService.getFacturaPendientes());
    }

}
