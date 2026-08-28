package org.example.energy.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.zona.ZonaResponseDTO;
import org.example.energy.service.ZonaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@Tag(name = "Zonas")
@RequestMapping("/api/v1/zonas")
public class ZonaController {

    private final ZonaService zonaService;

    @GetMapping
    public ResponseEntity<List<ZonaResponseDTO>> getAll(){
        return ResponseEntity.ok(zonaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZonaResponseDTO> getById(@PathVariable Integer id){
        return ResponseEntity.ok(zonaService.findById(id));
    }
}
