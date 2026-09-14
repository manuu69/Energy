package org.example.energy.zona.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.zona.dto.ZonaResponseDTO;
import org.example.energy.zona.service.ZonaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@Tag(name = "Zonas", description = "Endpoints para la consulta y catalogación de zonas de distribución eléctrica")
@RequestMapping("/zonas")
public class ZonaController {

    private final ZonaService zonaService;

    @GetMapping
    @Operation(summary = "Obtener todas las zonas", description = "Devuelve el listado completo de zonas de distribución configuradas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de zonas recuperada exitosamente")
    public ResponseEntity<List<ZonaResponseDTO>> getAll(){
        log.info("GET /api/v1/zonas - Solicitud para obtener todas las zonas");
        return ResponseEntity.ok(zonaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener zona por ID", description = "Devuelve la información detallada de una zona geográfica según su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Zona encontrada"),
            @ApiResponse(responseCode = "404", description = "Zona no encontrada")
    })
    public ResponseEntity<ZonaResponseDTO> getById(@PathVariable Integer id){
        log.info("GET /api/v1/zonas/{} - Obteniendo detalle de la zona", id);
        return ResponseEntity.ok(zonaService.findById(id));
    }

    /*@GetMapping("/{id}")
    public ResponseEntity<List<ZonaResponseDTO>> getByPadreId(@PathVariable Integer id){
        log.info("GET /api/v1/zonas/{} - Obteniendo subzonas", id);
        return ResponseEntity.ok(zonaService.findSubzonas(id));
    }*/
}