package org.example.energy.contrato.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.contrato.dto.ContratoCreateDTO;
import org.example.energy.contrato.dto.ContratoResponseDTO;
import org.example.energy.contrato.dto.ContratoUpdateDTO;
import org.example.energy.contrato.service.ContratoService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "Contratos")
@RequestMapping("/api/v1/contratos")
public class ContratoController {

    private final ContratoService contratoService;

    @GetMapping
    public ResponseEntity<Page<ContratoResponseDTO>> getAll(
            @ParameterObject
            @PageableDefault(
                    sort = "contratoId",
                    direction = Sort.Direction.ASC)
            Pageable pageable
    ){
        return ResponseEntity.ok(contratoService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoResponseDTO> getById(@PathVariable Integer id){
        return ResponseEntity.ok(contratoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ContratoResponseDTO> create(@RequestBody @Valid ContratoCreateDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(contratoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratoResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid ContratoUpdateDTO dto){
        return ResponseEntity.ok().body(contratoService.update(id, dto));
    }

    @PatchMapping("/{id}/baja")
    public ResponseEntity<ContratoResponseDTO> darBaja(@PathVariable Integer id){
        return ResponseEntity.ok(contratoService.darBaja(id));
    }

    @PatchMapping("/{id}/suspension")
    public ResponseEntity<ContratoResponseDTO> suspender(@PathVariable Integer id){
        return ResponseEntity.ok(contratoService.suspender(id));
    }

    @PatchMapping("/{id}/activacion")
    public ResponseEntity<ContratoResponseDTO> activar(@PathVariable Integer id){
        return ResponseEntity.ok(contratoService.activar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        contratoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
