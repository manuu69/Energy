package org.example.energy.lectura.controiler;


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

@Tag(name = "Lecturas")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/lecturas")
public class LecturaController {

    private final LecturaService lecturaService;

    @GetMapping
    public ResponseEntity<Page<LecturaResponseDTO>> getAll(
            @ParameterObject
            @PageableDefault(
                    sort = "lecturaId",
                    direction = Sort.Direction.ASC)
            Pageable pageable
    ){
        return ResponseEntity.ok(lecturaService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LecturaResponseDTO> getById(@PathVariable Integer id){
        return ResponseEntity.ok(lecturaService.getById(id));
    }

    @GetMapping("/contrato/{id}")
    public ResponseEntity<Page<LecturaResponseDTO>> getByContratoId(
            @PathVariable Integer id,
            @ParameterObject
            @PageableDefault(
                    sort = "lecturaId",
                    direction = Sort.Direction.ASC)
            Pageable pageable)
    {
        return ResponseEntity.ok(lecturaService.getByContratoId(id, pageable));
    }

    @PostMapping
    public ResponseEntity<LecturaResponseDTO> create(@RequestBody @Valid LecturaCreateDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(lecturaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LecturaResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid LecturaUpdateDTO dto){
        return ResponseEntity.ok().body(lecturaService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id){
        lecturaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/analisis")
    public ResponseEntity<List<LecturaAnalisisDTO>> getAnalisis() {
        return ResponseEntity.ok(lecturaService.getAnalisis());
    }

    @GetMapping("/analisis/contrato/{id}")
    public ResponseEntity<List<LecturaAnalisisDTO>> getAnalisisByContrato(
            @PathVariable Integer id) {
        return ResponseEntity.ok(lecturaService.getAnalisisByContrato(id));
    }

    @GetMapping("/analisis/anomalias")
    public ResponseEntity<List<LecturaAnalisisDTO>> getAnomalias(
            @RequestParam(required = false) BigDecimal umbral) {
        return ResponseEntity.ok(lecturaService.getAnomalias(umbral));
    }
}
