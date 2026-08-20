package org.example.energy.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.FacturaCreateDTO;
import org.example.energy.dto.FacturaResponseDTO;
import org.example.energy.service.serviceImpl.FacturaServiceImpl;
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
        log.info("GET /facturas/{} - Consultando factura", id);
        return ResponseEntity.ok(facturaService.getById(id));
    }

    @GetMapping("contrato/{id}")
    public ResponseEntity<List<FacturaResponseDTO>> getByContratoId(@PathVariable Integer id){
        return ResponseEntity.ok(facturaService.getByContratoId(id));
    }

    @PostMapping
    public ResponseEntity<FacturaResponseDTO> create(@RequestBody @Valid FacturaCreateDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(facturaService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("DELETE /facturas/{} - Solicitud de eliminación de factura", id);
        facturaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<FacturaResponseDTO> pagarFactura(@PathVariable Integer id) {
        log.info("PATCH /facturas/{}/pagar - Solicitud de pago de factura", id);
        return ResponseEntity.ok(facturaService.pagarFactura(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<FacturaResponseDTO> cancelarFactura(@PathVariable Integer id) {
        log.info("PATCH /facturas/{}/cancelar - Solicitud de cancelación de factura", id);
        return ResponseEntity.ok(facturaService.cancelarFactura(id));
    }

    @PostMapping("/generar/{mes}")
    public ResponseEntity<Void> generarFacturas(@PathVariable Integer mes) {
        log.info("POST /facturas/generar/{} - Solicitud de generación masiva", mes);
        facturaService.generarFacturas(mes);
        return ResponseEntity.noContent().build();
    }

}
