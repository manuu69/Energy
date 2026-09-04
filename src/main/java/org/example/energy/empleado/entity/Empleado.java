package org.example.energy.empleado.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "empleados")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "empleado_id")
    private Integer empleadoId;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "email", unique = true, nullable = false, length = 150)
    private String email;

    @Column(name = "departamento", nullable = false, length = 50)
    private String departamento;

    @Column(name = "rol", nullable = false, length = 50)
    private String rol;

    @Column(name = "salario", nullable = false, precision = 10, scale = 2)
    private BigDecimal salario;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    @Column(name = "activo")
    private Boolean activo = true;

    // Relación recursiva con sí misma (jefe)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jefe_id")
    private Empleado jefe;
}
