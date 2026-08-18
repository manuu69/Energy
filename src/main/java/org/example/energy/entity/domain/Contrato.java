package org.example.energy.entity.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.energy.enums.EstadoContrato;

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
    private String tarifa;

    @Column(name = "potencia_kw", precision = 6, scale = 2)
    private BigDecimal potenciaKw;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoContrato estado;

    // Relación con Zona
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id")
    private Zona zona;
}
