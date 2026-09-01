package org.example.energy.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.incidencia.IncidenciaResponseDTO;
import org.example.energy.enums.EstadoIncidencia;
import org.example.energy.enums.TipoIncidencia;
import org.example.energy.service.IncidenciaService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
