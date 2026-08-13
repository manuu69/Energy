package org.example.energy.entity.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "lecturas")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lectura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lectura_id")
    private Integer lecturaId;

    // Relación con Contrato
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "consumo_kwh", nullable = false, precision = 10, scale = 2)
    private BigDecimal consumoKwh;

    @Column(name = "tipo_lectura", length = 20)
    private String tipoLectura;  // real, estimada, autoconsumo
}
