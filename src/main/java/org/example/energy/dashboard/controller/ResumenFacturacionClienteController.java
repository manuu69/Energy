package org.example.energy.dashboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dashboard.dto.ResumenFacturacionClienteResponseDTO;
import org.example.energy.dashboard.service.ResumenFacturacionClienteService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Resumen Facturación Cliente", description = "Endpoints de consulta para resúmenes e historial de facturación consolidado por cliente")
@RestController
@RequestMapping("/resumenes/facturacion-clientes")
@RequiredArgsConstructor
public class ResumenFacturacionClienteController {

    private final ResumenFacturacionClienteService service;

    @GetMapping
    @Operation(summary = "Obtener resúmenes de facturación paginados", description = "Devuelve una lista paginada con el histórico y estado consolidado de facturación de los clientes.")
    @ApiResponse(responseCode = "200", description = "Página de resúmenes de facturación recuperada exitosamente")
    public ResponseEntity<Page<ResumenFacturacionClienteResponseDTO>> getAll(
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "clienteId",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        log.info("Get all llamado de ");
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/{clienteId}")
    @Operation(summary = "Obtener resumen de facturación por ID de cliente", description = "Retorna el resumen detallado de facturación acumulada para un cliente específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resumen del cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe resumen de facturación para el ID de cliente especificado")
    })
    public ResponseEntity<ResumenFacturacionClienteResponseDTO> getByClienteId(
            @PathVariable Integer clienteId
    ) {
        return ResponseEntity.ok(
                service.getByClienteId(clienteId)
        );
    }
}