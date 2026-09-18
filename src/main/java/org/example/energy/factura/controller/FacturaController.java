package org.example.energy.factura.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.factura.dto.FacturaCreateDTO;
import org.example.energy.factura.dto.FacturaFilter;
import org.example.energy.factura.dto.FacturaResponseDTO;
import org.example.energy.factura.service.FacturaService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Tag(name = "Facturas", description = "Endpoints para la gestión, emisión, cobro y generación masiva de facturas")
@RestController
@RequestMapping("/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    @GetMapping("/export/csv")
    @Operation(summary = "Exportar facturas filtradas a CSV mediante streaming")
    public ResponseEntity<StreamingResponseBody> exportCsv(@ParameterObject FacturaFilter filter) {
        String filename = "facturas_" + LocalDate.now() + ".csv";

        StreamingResponseBody responseBody = facturaService.exportToCsv(filter);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(responseBody);
    }


    @GetMapping
    @Operation(summary = "Obtener todas las facturas", description = "Devuelve una lista paginada de todas las facturas registradas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Página de facturas recuperada exitosamente")
    public ResponseEntity<Page<FacturaResponseDTO>> getAll(
            @ParameterObject FacturaFilter filter,
            @ParameterObject
            @PageableDefault(
                    sort = "facturaId",
                    direction = Sort.Direction.ASC)
            Pageable pageable
    ){
        log.info("GET /api/v1/facturas - Obteniendo facturas paginadas");
        return ResponseEntity.ok(facturaService.getAll(filter, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener factura por ID", description = "Devuelve la información detallada de una factura por su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Factura encontrada"),
            @ApiResponse(responseCode = "404", description = "No se encontró ninguna factura con el ID especificado")
    })
    public ResponseEntity<FacturaResponseDTO> getById(@PathVariable Integer id){
        log.info("GET /api/v1/facturas/{} - Consultando factura", id);
        return ResponseEntity.ok(facturaService.getById(id));
    }

    @GetMapping("/contrato/{id}")
    @Operation(summary = "Obtener facturas por ID de contrato", description = "Retorna el listado de facturas asociadas a un contrato específico.")
    @ApiResponse(responseCode = "200", description = "Lista de facturas del contrato recuperada exitosamente")
    public ResponseEntity<List<FacturaResponseDTO>> getByContratoId(@PathVariable Integer id){
        log.info("GET /api/v1/facturas/contrato/{} - Consultando facturas por contrato", id);
        return ResponseEntity.ok(facturaService.getByContratoId(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva factura", description = "Emite una nueva factura individual tras validar los datos de entrada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Factura creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<FacturaResponseDTO> create(@RequestBody @Valid FacturaCreateDTO dto){
        log.info("POST /api/v1/facturas - Creando nueva factura para contrato ID: {}", dto.contratoId());
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaService.create(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una factura", description = "Elimina físicamente el registro de una factura por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Factura eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("DELETE /api/v1/facturas/{} - Solicitud de eliminación de factura", id);
        facturaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/pagar")
    @Operation(summary = "Registrar pago de una factura", description = "Actualiza el estado de la factura a 'PAGADA' y registra la fecha de cobro.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago registrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    public ResponseEntity<FacturaResponseDTO> pagarFactura(@PathVariable Integer id) {
        log.info("PATCH /api/v1/facturas/{}/pagar - Solicitud de pago de factura", id);
        return ResponseEntity.ok(facturaService.pagarFactura(id));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar una factura", description = "Modifica el estado de una factura emitida a 'CANCELADA'.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Factura cancelada correctamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    public ResponseEntity<FacturaResponseDTO> cancelarFactura(@PathVariable Integer id) {
        log.info("PATCH /api/v1/facturas/{}/cancelar - Solicitud de cancelación de factura", id);
        return ResponseEntity.ok(facturaService.cancelarFactura(id));
    }

    @PostMapping("/generar/{mes}")
    @Operation(summary = "Generación masiva de facturas", description = "Inicia el proceso automatizado de emisión masiva de facturas para todos los contratos activos en un mes específico.")
    @ApiResponse(responseCode = "204", description = "Proceso de generación masiva ejecutado correctamente")
    public ResponseEntity<Void> generarFacturas(@PathVariable Integer mes) {
        log.info("POST /api/v1/facturas/generar/{} - Solicitud de generación masiva", mes);
        facturaService.generarFacturas(mes);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/marcar-facturas-vencidas")
    @Operation(summary = "Actualizar estado de facturas vencidas", description = "Busca facturas pendientes cuya fecha límite haya expirado y actualiza su estado a 'VENCIDA'.")
    @ApiResponse(responseCode = "200", description = "Número de facturas marcadas como vencidas")
    public ResponseEntity<Integer> marcarFactuasVencidas(){
        log.info("PUT /api/v1/facturas/marcar-facturas-vencidas - Ejecutando actualización de facturas vencidas");
        return ResponseEntity.ok(facturaService.actualizarFacturasVencidas());
    }

}