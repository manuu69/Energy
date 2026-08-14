package org.example.energy.entity.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "historico_tarifas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HistoricoTarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historico_id")
    private Integer historicoId;

    // Relación con Contrato
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @Column(name = "tarifa", nullable = false, length = 10)
    private String tarifa;

    @Column(name = "precio_kwh", nullable = false, precision = 6, scale = 4)
    private BigDecimal precioKwh;

    @Column(name = "potencia_kw", nullable = false, precision = 6, scale = 2)
    private BigDecimal potenciaKw;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;  // NULL = tarifa vigente

    @Column(name = "motivo_cambio", length = 100)
    private String motivoCambio;
}
