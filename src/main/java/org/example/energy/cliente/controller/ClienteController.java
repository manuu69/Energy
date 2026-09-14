package org.example.energy.cliente.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Clientes", description = "Endpoints para la gestión, consulta, filtrado y baja de clientes")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Obtener todos los clientes", description = "Devuelve una lista paginada de todos los clientes registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Página de clientes recuperada exitosamente")
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
    @Operation(summary = "Obtener cliente por ID", description = "Devuelve la información detallada de un cliente a partir de su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado para el ID especificado")
    })
    public ResponseEntity<ClienteResponseDTO> getById(@PathVariable Integer id){
        log.info("Solicitud de cliente por id");
        return ResponseEntity.ok(clienteService.getById(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Obtener cliente por correo electrónico", description = "Busca y retorna un cliente registrado según su dirección de email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe ningún cliente con el correo electrónico proporcionado")
    })
    public ResponseEntity<ClienteResponseDTO> getByEmail(@PathVariable String email) {
        log.info("Solicitud de cliente por email");
        return ResponseEntity.ok(clienteService.getByEmail(email));
    }

    @GetMapping("/ciudad/{ciudad}")
    @Operation(summary = "Filtrar clientes por ciudad", description = "Obtiene una lista paginada de clientes ubicados en la ciudad especificada.")
    @ApiResponse(responseCode = "200", description = "Página de clientes en la ciudad recuperada exitosamente")
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
    @Operation(summary = "Filtrar clientes por segmento comercial", description = "Devuelve los clientes agrupados por su segmento asignado (NUEVO, REGULAR, PREMIUM, VIP).")
    @ApiResponse(responseCode = "200", description = "Página de clientes filtrados por segmento recuperada exitosamente")
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
    @Operation(summary = "Filtrar clientes por tipo de cliente", description = "Retorna una página de clientes filtrada según su tipo de contrato/suministro.")
    @ApiResponse(responseCode = "200", description = "Página de clientes filtrados por tipo recuperada exitosamente")
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
    @Operation(summary = "Registrar un nuevo cliente", description = "Crea un nuevo registro de cliente tras validar el objeto recibido.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Petición inválida debido a errores de validación en los datos de entrada")
    })
    public ResponseEntity<ClienteResponseDTO> create(@RequestBody @Valid ClienteCreateDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de un cliente", description = "Modifica los datos de un cliente existente identificado por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid ClienteUpdateDTO dto){
        return ResponseEntity.ok().body(clienteService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Dar de baja un cliente", description = "Realiza el proceso de baja o eliminación de un cliente según su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente dado de baja exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        clienteService.darBaja(id);
        return ResponseEntity.noContent().build();
    }
}