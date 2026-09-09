package org.example.energy.empleado.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Empleados")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoController {
}
