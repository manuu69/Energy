package org.example.energy.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.cliente.ClienteCreateDTO;
import org.example.energy.dto.cliente.ClienteResponseDTO;
import org.example.energy.enums.Segmento;
import org.example.energy.enums.TipoCliente;
import org.example.energy.service.serviceImpl.ClienteServiceImpl;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@Tag(name = "Clientes")
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteServiceImpl clienteService;

    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> getAll(
            @ParameterObject
            @PageableDefault(
                    sort = "clienteId",
                    direction = Sort.Direction.ASC)
            Pageable pageable

    ){
        return ResponseEntity.ok(clienteService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> getById(@PathVariable Integer id){
        log.info("Solicitud de cliente por id");
        return ResponseEntity.ok(clienteService.getById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteResponseDTO> getByEmail(@PathVariable String email) {
        log.info("Solicitud de cliente por email");
        return ResponseEntity.ok(clienteService.getByEmail(email));
    }

    @GetMapping("/ciudad/{ciudad}")
    public ResponseEntity<Page<ClienteResponseDTO>> getByCiudad(
            @ParameterObject
            @PageableDefault(
                    sort = "clienteId",
                    direction = Sort.Direction.ASC)
            Pageable pageable, @PathVariable String ciudad

    ){
        log.info("Solicitud de cliente por ciudad");
        return ResponseEntity.ok(clienteService.getByCiudad(ciudad, pageable));
    }

    @GetMapping("/segmento/{segmento}")
    public ResponseEntity<Page<ClienteResponseDTO>> getBySegmento(
            @ParameterObject
            @PageableDefault(
                    sort = "clienteId",
                    direction = Sort.Direction.ASC)
            Pageable pageable, @PathVariable Segmento segmento

    ){
        log.info("Solicitud de cliente por segemento");
        return ResponseEntity.ok(clienteService.getBySegmento(segmento, pageable));
    }

    @GetMapping("/tipoCliente/{tipo}")
    public ResponseEntity<Page<ClienteResponseDTO>> getByTipo(
            @ParameterObject
            @PageableDefault(
                    sort = "clienteId",
                    direction = Sort.Direction.ASC)
            Pageable pageable, @PathVariable TipoCliente tipo

    ){
        log.info("Solicitud de cliente por tipo");
        return ResponseEntity.ok(clienteService.getByTipo(tipo, pageable));
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> create(@RequestBody @Valid ClienteCreateDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.create(dto));
    }
}
