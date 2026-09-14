package org.example.energy.contrato.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.contrato.dto.ContratoCreateDTO;
import org.example.energy.contrato.dto.ContratoResponseDTO;
import org.example.energy.contrato.dto.ContratoUpdateDTO;
import org.example.energy.contrato.service.ContratoService;
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
@Tag(name = "Contratos", description = "Endpoints para la gestión, modificación de condiciones y ciclo de vida de los contratos de suministro")
@RequestMapping("/contratos")
public class ContratoController {

    private final ContratoService contratoService;

    @GetMapping
    @Operation(summary = "Obtener todos los contratos", description = "Devuelve una lista paginada de todos los contratos de suministro registrados en el sistema.")
    @ApiResponse(responseCode = "200", description = "Página de contratos recuperada exitosamente")
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
    @Operation(summary = "Obtener contrato por ID", description = "Retorna la información detallada de un contrato especificando su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrato encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe ningún contrato con el ID especificado")
    })
    public ResponseEntity<ContratoResponseDTO> getById(@PathVariable Integer id){
        return ResponseEntity.ok(contratoService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo contrato", description = "Registra un nuevo contrato de suministro tras validar los datos de entrada requeridos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Contrato registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Petición inválida por errores de validación en los datos de entrada")
    })
    public ResponseEntity<ContratoResponseDTO> create(@RequestBody @Valid ContratoCreateDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(contratoService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un contrato", description = "Modifica los parámetros (zona, tarifa, potencia) de un contrato existente por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrato actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "404", description = "Contrato no encontrado")
    })
    public ResponseEntity<ContratoResponseDTO> update(@PathVariable Integer id, @RequestBody @Valid ContratoUpdateDTO dto){
        return ResponseEntity.ok().body(contratoService.update(id, dto));
    }

    @PatchMapping("/{id}/baja")
    @Operation(summary = "Dar de baja un contrato", description = "Cambia el estado del contrato a 'BAJA' de forma permanente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrato dado de baja correctamente"),
            @ApiResponse(responseCode = "404", description = "Contrato no encontrado")
    })
    public ResponseEntity<ContratoResponseDTO> darBaja(@PathVariable Integer id){
        return ResponseEntity.ok(contratoService.darBaja(id));
    }

    @PatchMapping("/{id}/suspension")
    @Operation(summary = "Suspender temporalmente un contrato", description = "Cambia el estado del contrato a 'SUSPENDIDO' (por ejemplo, por impagados o mantenimiento).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrato suspendido correctamente"),
            @ApiResponse(responseCode = "404", description = "Contrato no encontrado")
    })
    public ResponseEntity<ContratoResponseDTO> suspender(@PathVariable Integer id){
        return ResponseEntity.ok(contratoService.suspender(id));
    }

    @PatchMapping("/{id}/activacion")
    @Operation(summary = "Reactivar un contrato", description = "Reestablece el estado de un contrato suspendido a 'ACTIVO'.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contrato activado correctamente"),
            @ApiResponse(responseCode = "404", description = "Contrato no encontrado")
    })
    public ResponseEntity<ContratoResponseDTO> activar(@PathVariable Integer id){
        return ResponseEntity.ok(contratoService.activar(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un contrato", description = "Elimina físicamente el registro del contrato del sistema por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contrato eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Contrato no encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        contratoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}