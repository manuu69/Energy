package org.example.energy.entity.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contratos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contrato_id")
    private Integer contratoId;

    // Relación con Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "tarifa", length = 10)
    private String tarifa;  // 2.0TD, 3.0TD, 6.1TD, 3.1TD

    @Column(name = "potencia_kw", precision = 6, scale = 2)
    private BigDecimal potenciaKw;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "estado", length = 20)
    private String estado;  // activo, baja, suspendido

    // Relación con Zona
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id")
    private Zona zona;
}
