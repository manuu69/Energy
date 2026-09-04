package org.example.energy.cliente.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.cliente.dto.ClienteCreateDTO;
import org.example.energy.cliente.dto.ClienteResponseDTO;
import org.example.energy.cliente.dto.ClienteUpdateDTO;
import org.example.energy.common.enums.Segmento;
import org.example.energy.common.enums.TipoCliente;
import org.example.energy.cliente.service.ClienteService;
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

    private final ClienteService clienteService;

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

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid ClienteUpdateDTO dto){
        return ResponseEntity.ok().body(clienteService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        clienteService.darBaja(id);
        return ResponseEntity.noContent().build();
    }
}
