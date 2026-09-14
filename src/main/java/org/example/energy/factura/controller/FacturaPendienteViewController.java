package org.example.energy.factura.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.factura.dto.FacturaResponseDTO;
import org.example.energy.factura.service.FacturaPendienteViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/facturas/pendientes")
@Tag(name = "Facturas Pendientes", description = "Endpoints de consulta optimizados para facturas pendientes de cobro")
public class FacturaPendienteViewController {

    private final FacturaPendienteViewService facturaService;

    @GetMapping
    @Operation(summary = "Obtener facturas pendientes de cobro", description = "Devuelve el listado completo de facturas cuyo estado de pago está en pendiente.")
    @ApiResponse(responseCode = "200", description = "Listado de facturas pendientes recuperado exitosamente")
    public ResponseEntity<List<FacturaResponseDTO>> getFacturasPendientes(){
        log.info("GET /api/v1/facturas/pendientes - Consultando listado de facturas pendientes");
        return ResponseEntity.ok(facturaService.getFacturaPendientes());
    }

}