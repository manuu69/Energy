package org.example.energy.incidencia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.common.annotation.ApiCreateResponses;
import org.example.energy.incidencia.dto.IncidenciaCreateDTO;
import org.example.energy.incidencia.dto.IncidenciaCriticaDTO;
import org.example.energy.incidencia.dto.IncidenciaResponseDTO;
import org.example.energy.incidencia.dto.IncidenciaUpdateDTO;
import org.example.energy.common.enums.EstadoIncidencia;
import org.example.energy.common.enums.TipoIncidencia;
import org.example.energy.incidencia.service.IncidenciaService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Incidencias", description = "Endpoints para el reporte, seguimiento, gestión y resolución de incidencias técnicas")
@AllArgsConstructor
@RestController
@RequestMapping("/incidencias")
public class IncidenciaController {

    private final IncidenciaService incidenciaService;

    @GetMapping
    @Operation(summary = "Obtener todas las incidencias", description = "Retorna una página con el listado completo de incidencias.")
    @ApiResponse(responseCode = "200", description = "Página de incidencias recuperada exitosamente")
    public ResponseEntity<Page<IncidenciaResponseDTO>> getAll(
            @ParameterObject
            @PageableDefault(
                    sort = "incidenciaId",
                    direction = Sort.Direction.ASC)
            Pageable pageable
    ){
        log.info("GET /api/v1/incidencias - Obteniendo todas las incidencias paginadas");
        return ResponseEntity.ok().body(incidenciaService.getAll(pageable));
    }

    @GetMapping("/contrato/{id}")
    @Operation(summary = "Obtener incidencias por contrato", description = "Devuelve las incidencias asociadas a un contrato en particular.")
    @ApiResponse(responseCode = "200", description = "Incidencias por contrato recuperadas exitosamente")
    public ResponseEntity<Page<IncidenciaResponseDTO>> getByContratoId(
            @PathVariable Integer id,
            @ParameterObject
            @PageableDefault(
                    sort = "incidenciaId",
                    direction = Sort.Direction.ASC)
            Pageable pageable
    ){
        log.info("GET /api/v1/incidencias/contrato/{} - Consultando incidencias por contrato ID", id);
        return ResponseEntity.ok().body(incidenciaService.getByContratoId(id, pageable));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Filtrar incidencias por estado", description = "Obtiene las incidencias filtradas según su estado actual (ABIERTA, EN_PROCESO, RESUELTA, etc.).")
    @ApiResponse(responseCode = "200", description = "Incidencias por estado recuperadas exitosamente")
    public ResponseEntity<Page<IncidenciaResponseDTO>> getByEstado(
            @PathVariable EstadoIncidencia estado,
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "incidenciaId",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        log.info("GET /api/v1/incidencias/estado/{} - Consultando incidencias por estado", estado);
        return ResponseEntity.ok(
                incidenciaService.getByEstado(estado, pageable)
        );
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Filtrar incidencias por tipo", description = "Devuelve un listado paginado de incidencias pertenecientes a una categoría específica.")
    @ApiResponse(responseCode = "200", description = "Incidencias por tipo recuperadas exitosamente")
    public ResponseEntity<Page<IncidenciaResponseDTO>> getByTipo(
            @PathVariable TipoIncidencia tipo,
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "incidenciaId",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        log.info("GET /api/v1/incidencias/tipo/{} - Consultando incidencias por tipo", tipo);
        return ResponseEntity.ok(
                incidenciaService.getByTipo(tipo, pageable)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener incidencia por ID", description = "Retorna la información detallada de una incidencia por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Incidencia encontrada"),
            @ApiResponse(responseCode = "404", description = "Incidencia no encontrada")
    })
    public ResponseEntity<IncidenciaResponseDTO> getById(@PathVariable Integer id){
        log.info("GET /api/v1/incidencias/{} - Obteniendo detalle de incidencia", id);
        return ResponseEntity.ok().body(incidenciaService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar nueva incidencia", description = "Crea un nuevo reporte de incidencia asociado a un contrato.")
    @ApiCreateResponses
    public ResponseEntity<IncidenciaResponseDTO> create(
            @RequestBody @Valid IncidenciaCreateDTO dto
    ) {
        log.info("POST /api/v1/incidencias - Registrando incidencia para contrato ID: {}", dto.contratoId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(incidenciaService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar incidencia", description = "Modifica los datos de una incidencia existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Incidencia actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Incidencia no encontrada")
    })
    public ResponseEntity<IncidenciaResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid IncidenciaUpdateDTO dto){
        log.info("PUT /api/v1/incidencias/{} - Actualizando incidencia", id);
        return ResponseEntity.ok().body(incidenciaService.update(id, dto));
    }

    @PatchMapping("/{id}/iniciarGestion")
    @Operation(summary = "Iniciar gestión de incidencia", description = "Pasa la incidencia al estado 'EN_PROCESO' asignándole inicio de gestión.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gestión iniciada correctamente"),
            @ApiResponse(responseCode = "404", description = "Incidencia no encontrada")
    })
    public ResponseEntity<IncidenciaResponseDTO> iniciarGestion(@PathVariable Integer id){
        log.info("PATCH /api/v1/incidencias/{}/iniciarGestion - Iniciando gestión", id);
        return ResponseEntity.ok().body(incidenciaService.iniciarGestion(id));
    }

    @PatchMapping("/{id}/cerrar")
    @Operation(summary = "Cerrar incidencia", description = "Finaliza el flujo marcando la incidencia como resuelta/cerrada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Incidencia cerrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Incidencia no encontrada")
    })
    public ResponseEntity<IncidenciaResponseDTO> cerrar(@PathVariable Integer id){
        log.info("PATCH /api/v1/incidencias/{}/cerrar - Solicitud de cierre de incidencia", id);
        return ResponseEntity.ok().body(incidenciaService.cerrar(id));
    }

    @GetMapping("/criticas")
    @Operation(summary = "Obtener incidencias críticas", description = "Devuelve el listado de incidencias etiquetadas como críticas o de alta prioridad.")
    @ApiResponse(responseCode = "200", description = "Incidencias críticas recuperadas exitosamente")
    public ResponseEntity<List<IncidenciaCriticaDTO>> getCriticas() {
        log.info("GET /api/v1/incidencias/criticas - Obteniendo incidencias críticas globales");
        return ResponseEntity.ok(incidenciaService.getIncidenciasCriticas());
    }

    @GetMapping("/criticas/contrato/{id}")
    @Operation(summary = "Obtener incidencias críticas por contrato", description = "Devuelve el listado de incidencias críticas pertenecientes a un contrato en particular.")
    @ApiResponse(responseCode = "200", description = "Incidencias críticas del contrato recuperadas exitosamente")
    public ResponseEntity<List<IncidenciaCriticaDTO>> getCriticasByContrato(
            @PathVariable Integer id) {
        log.info("GET /api/v1/incidencias/criticas/contrato/{} - Consultando incidencias críticas por contrato ID", id);
        return ResponseEntity.ok(incidenciaService.getIncidenciasCriticasByContrato(id));
    }
}