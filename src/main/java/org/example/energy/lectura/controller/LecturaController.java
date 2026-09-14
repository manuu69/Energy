package org.example.energy.lectura.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.lectura.dto.LecturaAnalisisDTO;
import org.example.energy.lectura.dto.LecturaCreateDTO;
import org.example.energy.lectura.dto.LecturaResponseDTO;
import org.example.energy.lectura.dto.LecturaUpdateDTO;
import org.example.energy.lectura.service.LecturaService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Lecturas", description = "Endpoints para el registro, consulta y análisis de lecturas de contadores y consumo energético")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/lecturas")
public class LecturaController {

    private final LecturaService lecturaService;

    @GetMapping
    @Operation(summary = "Obtener todas las lecturas", description = "Retorna una lista paginada de todas las lecturas registradas.")
    @ApiResponse(responseCode = "200", description = "Página de lecturas recuperada exitosamente")
    public ResponseEntity<Page<LecturaResponseDTO>> getAll(
            @ParameterObject
            @PageableDefault(
                    sort = "lecturaId",
                    direction = Sort.Direction.ASC)
            Pageable pageable
    ){
        log.info("GET /api/v1/lecturas - Solicitud de obtención de lecturas paginadas");
        return ResponseEntity.ok(lecturaService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener lectura por ID", description = "Retorna los detalles de una lectura a partir de su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lectura encontrada"),
            @ApiResponse(responseCode = "404", description = "Lectura no encontrada")
    })
    public ResponseEntity<LecturaResponseDTO> getById(@PathVariable Integer id){
        log.info("GET /api/v1/lecturas/{} - Solicitud de lectura por ID", id);
        return ResponseEntity.ok(lecturaService.getById(id));
    }

    @GetMapping("/contrato/{id}")
    @Operation(summary = "Obtener lecturas por contrato", description = "Retorna una página de lecturas asociadas a un contrato en específico.")
    @ApiResponse(responseCode = "200", description = "Lecturas del contrato recuperadas exitosamente")
    public ResponseEntity<Page<LecturaResponseDTO>> getByContratoId(
            @PathVariable Integer id,
            @ParameterObject
            @PageableDefault(
                    sort = "lecturaId",
                    direction = Sort.Direction.ASC)
            Pageable pageable)
    {
        log.info("GET /api/v1/lecturas/contrato/{} - Obteniendo lecturas por contrato ID", id);
        return ResponseEntity.ok(lecturaService.getByContratoId(id, pageable));
    }

    @PostMapping
    @Operation(summary = "Registrar nueva lectura", description = "Registra una nueva lectura de contador tras validar los datos de entrada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lectura registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de lectura inválidos")
    })
    public ResponseEntity<LecturaResponseDTO> create(@RequestBody @Valid LecturaCreateDTO dto){
        log.info("POST /api/v1/lecturas - Registrando nueva lectura para contrato ID: {}", dto.contratoId());
        return ResponseEntity.status(HttpStatus.CREATED).body(lecturaService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar lectura", description = "Modifica los datos de una lectura existente por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lectura actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de lectura inválidos"),
            @ApiResponse(responseCode = "404", description = "Lectura no encontrada")
    })
    public ResponseEntity<LecturaResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid LecturaUpdateDTO dto){
        log.info("PUT /api/v1/lecturas/{} - Solicitud de actualización de lectura", id);
        return ResponseEntity.ok().body(lecturaService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar lectura", description = "Elimina un registro de lectura por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Lectura eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Lectura no encontrada")
    })
    public ResponseEntity<Void> deleteById(@PathVariable Integer id){
        log.info("DELETE /api/v1/lecturas/{} - Eliminando lectura", id);
        lecturaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/analisis")
    @Operation(summary = "Obtener análisis general de lecturas", description = "Devuelve el análisis consolidado sobre consumos y patrones de lecturas.")
    @ApiResponse(responseCode = "200", description = "Análisis de lecturas obtenido exitosamente")
    public ResponseEntity<List<LecturaAnalisisDTO>> getAnalisis() {
        log.info("GET /api/v1/lecturas/analisis - Obteniendo análisis general de lecturas");
        return ResponseEntity.ok(lecturaService.getAnalisis());
    }

    @GetMapping("/analisis/contrato/{id}")
    @Operation(summary = "Obtener análisis de lecturas por contrato", description = "Genera el análisis detallado de lecturas asociadas a un contrato específico.")
    @ApiResponse(responseCode = "200", description = "Análisis por contrato recuperado exitosamente")
    public ResponseEntity<List<LecturaAnalisisDTO>> getAnalisisByContrato(
            @PathVariable Integer id) {
        log.info("GET /api/v1/lecturas/analisis/contrato/{} - Obteniendo análisis para contrato ID", id);
        return ResponseEntity.ok(lecturaService.getAnalisisByContrato(id));
    }

    @GetMapping("/analisis/anomalias")
    @Operation(summary = "Detectar anomalías de consumo", description = "Devuelve una lista de lecturas con anomalías detectadas según un umbral opcional.")
    @ApiResponse(responseCode = "200", description = "Anomalías detectadas recuperadas exitosamente")
    public ResponseEntity<List<LecturaAnalisisDTO>> getAnomalias(
            @RequestParam(required = false) BigDecimal umbral) {
        log.info("GET /api/v1/lecturas/analisis/anomalias - Detectando anomalías con umbral: {}", umbral);
        return ResponseEntity.ok(lecturaService.getAnomalias(umbral));
    }
}