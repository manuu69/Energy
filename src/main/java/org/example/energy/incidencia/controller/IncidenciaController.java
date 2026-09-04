package org.example.energy.incidencia.controller;

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
@Tag(name = "Incidencias")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/incidencias")
public class IncidenciaController {

    private final IncidenciaService incidenciaService;

    @GetMapping
    public ResponseEntity<Page<IncidenciaResponseDTO>> getAll(
            @ParameterObject
            @PageableDefault(
                    sort = "incidenciaId",
                    direction = Sort.Direction.ASC)
            Pageable pageable
    ){
        return ResponseEntity.ok().body(incidenciaService.getAll(pageable));
    }

    @GetMapping("/contrato/{id}")
    public ResponseEntity<Page<IncidenciaResponseDTO>> getByContratoId(
            @PathVariable Integer id,
            @ParameterObject
            @PageableDefault(
                    sort = "incidenciaId",
                    direction = Sort.Direction.ASC)
            Pageable pageable
    ){
        return ResponseEntity.ok().body(incidenciaService.getByContratoId(id, pageable));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<IncidenciaResponseDTO>> getByEstado(
            @PathVariable EstadoIncidencia estado,
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "incidenciaId",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(
                incidenciaService.getByEstado(estado, pageable)
        );
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Page<IncidenciaResponseDTO>> getByTipo(
            @PathVariable TipoIncidencia tipo,
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "incidenciaId",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(
                incidenciaService.getByTipo(tipo, pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidenciaResponseDTO> getById(@PathVariable Integer id){
        return ResponseEntity.ok().body(incidenciaService.getById(id));
    }

    @PostMapping
    @ApiCreateResponses
    public ResponseEntity<IncidenciaResponseDTO> create(
            @RequestBody @Valid IncidenciaCreateDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(incidenciaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidenciaResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid IncidenciaUpdateDTO dto){
        return ResponseEntity.ok().body(incidenciaService.update(id, dto));
    }

    @PutMapping("/{id}/iniciarGestion")
    public ResponseEntity<IncidenciaResponseDTO> iniciarGestion(@PathVariable Integer id){
        return ResponseEntity.ok().body(incidenciaService.iniciarGestion(id));
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<IncidenciaResponseDTO> cerrar(@PathVariable Integer id){
        return ResponseEntity.ok().body(incidenciaService.cerrar(id));
    }

    @GetMapping("/criticas")
    public ResponseEntity<List<IncidenciaCriticaDTO>> getCriticas() {
        return ResponseEntity.ok(incidenciaService.getIncidenciasCriticas());
    }

    @GetMapping("/criticas/contrato/{id}")
    public ResponseEntity<List<IncidenciaCriticaDTO>> getCriticasByContrato(
            @PathVariable Integer id) {
        return ResponseEntity.ok(incidenciaService.getIncidenciasCriticasByContrato(id));
    }
}
