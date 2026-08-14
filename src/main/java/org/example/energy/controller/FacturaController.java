package org.example.energy.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.energy.dto.FacturaResponseDTO;
import org.example.energy.service.serviceImpl.FacturaServiceImpl;
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

import java.util.List;


@AllArgsConstructor
@Tag(name = "Facturas")
@RestController
@RequestMapping("/api/v1/facturas")
public class FacturaController {

    private final FacturaServiceImpl facturaService;

    @GetMapping
    public ResponseEntity<Page<FacturaResponseDTO>> getAll(
            @ParameterObject
                @PageableDefault(
                        sort = "facturaId",
                                direction = Sort.Direction.ASC)
                            Pageable pageable

    ){
        return ResponseEntity.ok(facturaService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacturaResponseDTO> getById(@PathVariable Integer id){
        return ResponseEntity.ok(facturaService.getById(id));
    }

    @GetMapping("contrato/{id}")
    public ResponseEntity<List<FacturaResponseDTO>> getByContratoId(@PathVariable Integer id){
        return ResponseEntity.ok(facturaService.getByContratoId(id));
    }

}
