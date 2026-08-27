package org.example.energy.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.contrato.ContratoCreateDTO;
import org.example.energy.dto.contrato.ContratoResponseDTO;
import org.example.energy.service.ContratoService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
