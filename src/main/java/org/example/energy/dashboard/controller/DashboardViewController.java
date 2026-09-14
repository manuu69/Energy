package org.example.energy.dashboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.common.enums.TipoCliente;
import org.example.energy.dashboard.dto.*;
import org.example.energy.dashboard.service.DashboardResumenService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@Tag(name = "Dashboard", description = "Endpoints de analítica, agregados y métricas globales del sistema")
@AllArgsConstructor
@RestController
@RequestMapping("/dashboard")
public class DashboardViewController {

    private final DashboardResumenService dashboardRService;

    @GetMapping("/resumen")
    @Operation(summary = "Obtener resumen global del dashboard", description = "Retorna los indicadores clave globales del sistema (total de clientes, facturas emitidas, consumo acumulado, etc.).")
    @ApiResponse(responseCode = "200", description = "Resumen del dashboard recuperado exitosamente")
    public ResponseEntity<DashboardResumenDTO> getResumen(){
        return ResponseEntity.ok().body(dashboardRService.getResumen());
    }

    @GetMapping("/top-deudores")
    @Operation(summary = "Obtener top de clientes deudores", description = "Devuelve un listado con los mayores deudores del sistema limitado por la cantidad especificada.")
    @ApiResponse(responseCode = "200", description = "Listado de deudores recuperado exitosamente")
    public ResponseEntity<List<TopDeudorDTO>> getDeudores(@RequestParam(defaultValue = "5") int limit){
        return ResponseEntity.ok().body(dashboardRService.getDeudores(limit));
    }

    @GetMapping("/facturacion-mensual")
    @Operation(summary = "Obtener facturación mensual", description = "Retorna las métricas de facturación correspondientes al número de mes indicado (1-12).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos de facturación mensual recuperados correctamente"),
            @ApiResponse(responseCode = "400", description = "El número de mes proporcionado no está entre 1 y 12")
    })
    public ResponseEntity<FacturacionMensualDTO> getFacturacionMensual(
            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "El mes debe ser como mínimo 1")
            @Max(value = 12, message = "El mes debe ser como máximo 12")
            int mes){
        return ResponseEntity.ok().body(dashboardRService.getFacturacionMensual(mes));
    }

    @GetMapping("/tipo-cliente")
    @Operation(summary = "Contar clientes por tipo", description = "Devuelve el recuento total de clientes registrados filtrados por su categoría/tipo de cliente.")
    @ApiResponse(responseCode = "200", description = "Recuento de clientes obtenido exitosamente")
    public ResponseEntity<Long> getByTipo(
            @RequestParam TipoCliente tipoCliente
    ){
        return ResponseEntity.ok().body(dashboardRService.countByTipoCliente(tipoCliente));
    }

    @GetMapping("/tipo-incidencia")
    @Operation(summary = "Obtener desglose de incidencias por tipo", description = "Retorna la distribución y recuento de incidencias agrupadas por su tipología.")
    @ApiResponse(responseCode = "200", description = "Distribución de incidencias recuperada exitosamente")
    public ResponseEntity<List<IncidenciaPorTipoDTO>> getByIncidenciaTipo(){
        return ResponseEntity.ok().body(dashboardRService.getIncidenciaByTipo());
    }

    @GetMapping("/consumo-zona")
    @Operation(summary = "Obtener consumo por zona geográfica", description = "Muestra el total de consumo energético (kWh) agrupado por zonas de distribución.")
    @ApiResponse(responseCode = "200", description = "Métricas de consumo por zona recuperadas exitosamente")
    public ResponseEntity<List<ConsumoPorZonaDTO>> getConsumoPorZona(){
        return ResponseEntity.ok().body(dashboardRService.getConsumoPorZona());
    }
}