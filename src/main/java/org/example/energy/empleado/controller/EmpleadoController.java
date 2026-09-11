package org.example.energy.empleado.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

@Tag(name = "Empleados")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;


    @GetMapping
    public ResponseEntity<Page<EmpleadoResponseDTO>> getAll(
            @ParameterObject
            @PageableDefault(
                    sort = "empleadoId",
                    direction = Sort.Direction.ASC)
            Pageable pageable
    ){
        return ResponseEntity.ok(empleadoService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> getById(@PathVariable Integer id){
        return ResponseEntity.ok().body(empleadoService.getById(id));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<EmpleadoResponseDTO>> getByRol(@RequestParam RolEmpleado rol){
        return ResponseEntity.ok().body(empleadoService.getByRol(rol));
    }

    @GetMapping("/departamentos")
    public ResponseEntity<List<EmpleadoResponseDTO>> getByDepartamento(@RequestParam Departamento departamento){
        return ResponseEntity.ok().body(empleadoService.getByDepartamento(departamento));
    }

    @GetMapping("/subordinados/{id}")
    public ResponseEntity<List<EmpleadoResponseDTO>> getBySubordinado(@PathVariable Integer id){
        return ResponseEntity.ok().body(empleadoService.getBySubordinados(id));
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponseDTO> create(@Valid @RequestBody EmpleadoCreateDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.create(dto));  // devuelve 201
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> update(@PathVariable Integer id, @Valid @RequestBody EmpleadoUpdateDTO dto){
        return ResponseEntity.ok().body(empleadoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id){
        empleadoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
