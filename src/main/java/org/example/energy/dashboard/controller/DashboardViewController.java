package org.example.energy.dashboard.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.common.enums.TipoCliente;
import org.example.energy.dashboard.dto.*;
import org.example.energy.dashboard.service.DashboardResumenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Dashboard")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardViewController {

    private final DashboardResumenService dashboardRService;

    @GetMapping("/resumen")
    public ResponseEntity<DashboardResumenDTO> getResumen(){
        return ResponseEntity.ok().body(dashboardRService.getResumen());
    }

    @GetMapping("/top-deudores")
    public ResponseEntity<List<TopDeudorDTO>> getDeudores(@RequestParam(defaultValue = "5") int limit){
        return ResponseEntity.ok().body(dashboardRService.getDeudores(limit));
    }

    @GetMapping("/facturacion-mensual")
    public ResponseEntity<FacturacionMensualDTO> getFacturacionMensual(
            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "El mes debe ser como mínimo 1")
            @Max(value = 12, message = "El mes debe ser como máximo 12")
            int mes){
        return ResponseEntity.ok().body(dashboardRService.getFacturacionMensual(mes));
    }

    @GetMapping("/tipo-cliente")
    public ResponseEntity<Long> getByTipo(
            @RequestParam TipoCliente tipoCliente
    ){
        return ResponseEntity.ok().body(dashboardRService.countByTipoCliente(tipoCliente));
    }

    @GetMapping("/tipo-incidencia")
    public ResponseEntity<List<IncidenciaPorTipoDTO>> getByIncidenciaTipo(){
        return ResponseEntity.ok().body(dashboardRService.getIncidenciaByTipo());
    }

    @GetMapping("/consumo-zona")
    public ResponseEntity<List<ConsumoPorZonaDTO>> getConsumoPorZona(){
        return ResponseEntity.ok().body(dashboardRService.getConsumoPorZona());
    }
}
