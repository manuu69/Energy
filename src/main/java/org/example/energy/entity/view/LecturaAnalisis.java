package org.example.energy.entity.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vw_analisis_consumo_lecturas")
@Immutable
@Getter
@NoArgsConstructor
public class LecturaAnalisis {

    @Id
    @Column(name = "lectura_id")
    private Integer lecturaId;

    @Column(name = "contrato_id")
    private Integer contratoId;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "consumo_kwh")
    private BigDecimal consumoKwh;

    @Column(name = "diferencia_con_media")
    private BigDecimal diferenciaConMedia;
}
