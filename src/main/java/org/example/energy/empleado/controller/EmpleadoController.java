package org.example.energy.empleado.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.common.enums.Departamento;
import org.example.energy.common.enums.RolEmpleado;
import org.example.energy.empleado.dto.EmpleadoCreateDTO;
import org.example.energy.empleado.dto.EmpleadoResponseDTO;
import org.example.energy.empleado.dto.EmpleadoUpdateDTO;
import org.example.energy.empleado.service.EmpleadoService;
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
@Tag(name = "Empleados", description = "Endpoints para la gestión, jerarquía y asignación de personal interno")
@AllArgsConstructor
@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @GetMapping
    @Operation(summary = "Obtener todos los empleados", description = "Retorna una lista paginada de todos los empleados registrados en la plataforma.")
    @ApiResponse(responseCode = "200", description = "Página de empleados recuperada exitosamente")
    public ResponseEntity<Page<EmpleadoResponseDTO>> getAll(
            @ParameterObject
            @PageableDefault(
                    sort = "empleadoId",
                    direction = Sort.Direction.ASC)
            Pageable pageable
    ){
        log.info("Solicitud paginada para obtener todos los empleados");
        return ResponseEntity.ok(empleadoService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener empleado por ID", description = "Devuelve la información detallada de un empleado según su identificador único.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empleado encontrado"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado para el ID especificado")
    })
    public ResponseEntity<EmpleadoResponseDTO> getById(@PathVariable Integer id){
        log.info("Solicitud para obtener el empleado con ID: {}", id);
        return ResponseEntity.ok().body(empleadoService.getById(id));
    }

    @GetMapping("/roles")
    @Operation(summary = "Filtrar empleados por rol", description = "Obtiene la lista de empleados asociados a un rol específico dentro del sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de empleados filtrada por rol recuperada exitosamente")
    public ResponseEntity<List<EmpleadoResponseDTO>> getByRol(@RequestParam RolEmpleado rol){
        log.info("Solicitud para obtener empleados por rol: {}", rol);
        return ResponseEntity.ok().body(empleadoService.getByRol(rol));
    }

    @GetMapping("/departamentos")
    @Operation(summary = "Filtrar empleados por departamento", description = "Devuelve la lista de empleados pertenecientes al departamento especificado.")
    @ApiResponse(responseCode = "200", description = "Lista de empleados filtrada por departamento recuperada exitosamente")
    public ResponseEntity<List<EmpleadoResponseDTO>> getByDepartamento(@RequestParam Departamento departamento){
        log.info("Solicitud para obtener empleados por departamento: {}", departamento);
        return ResponseEntity.ok().body(empleadoService.getByDepartamento(departamento));
    }

    @GetMapping("/subordinados/{id}")
    @Operation(summary = "Obtener subordinados de un empleado", description = "Retorna el listado de empleados asignados bajo la supervisión directa del ID indicado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de subordinados recuperada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empleado supervisor no encontrado")
    })
    public ResponseEntity<List<EmpleadoResponseDTO>> getBySubordinado(@PathVariable Integer id){
        log.info("Solicitud para obtener los subordinados del empleado con ID: {}", id);
        return ResponseEntity.ok().body(empleadoService.getBySubordinados(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo empleado", description = "Crea un nuevo empleado en el sistema tras validar los datos de entrada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Empleado creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Petición inválida debido a errores de validación")
    })
    public ResponseEntity<EmpleadoResponseDTO> create(@Valid @RequestBody EmpleadoCreateDTO dto){
        log.info("Solicitud de creación para un nuevo empleado");
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de un empleado", description = "Modifica el perfil o la asignación de un empleado existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empleado actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    public ResponseEntity<EmpleadoResponseDTO> update(@PathVariable Integer id, @Valid @RequestBody EmpleadoUpdateDTO dto){
        log.info("Solicitud de actualización para el empleado con ID: {}", id);
        return ResponseEntity.ok().body(empleadoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un empleado", description = "Da de baja o elimina la ficha de un empleado del sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Empleado eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    public ResponseEntity<Void> deleteById(@PathVariable Integer id){
        log.info("Solicitud para eliminar el empleado con ID: {}", id);
        empleadoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}