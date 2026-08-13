package org.example.energy.entity.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Immutable
@Table(name = "vw_ultima_lectura")
public class UltimaLecturaView {

    @Id
    @Column(name = "lectura_id")
    private Integer lecturaId;

    @Column(name = "contrato_id")
    private Integer contratoId;

    private LocalDate fecha;

    @Column(name = "consumo_kwh")
    private BigDecimal consumoKwh;

    @Column(name = "tipo_lectura")
    private String tipoLectura;

}
