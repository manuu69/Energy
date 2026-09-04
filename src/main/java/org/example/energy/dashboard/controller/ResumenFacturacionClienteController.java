package org.example.energy.dashboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dashboard.dto.ResumenFacturacionClienteResponseDTO;
import org.example.energy.dashboard.service.ResumenFacturacionClienteService;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/resumenes/facturacion-clientes")
@RequiredArgsConstructor
public class ResumenFacturacionClienteController {

    private final ResumenFacturacionClienteService service;

    @GetMapping
    public ResponseEntity<Page<ResumenFacturacionClienteResponseDTO>> getAll(
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "clienteId",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<ResumenFacturacionClienteResponseDTO> getByClienteId(
            @PathVariable Integer clienteId
    ) {
        return ResponseEntity.ok(
                service.getByClienteId(clienteId)
        );
    }
}
