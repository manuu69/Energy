package org.example.energy.entity.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.energy.enums.EstadoIncidencia;
import org.example.energy.enums.TipoIncidencia;

import java.time.LocalDate;

@Entity
@Table(name = "incidencias")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "incidencia_id")
    private Integer incidenciaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 50)
    private TipoIncidencia tipo;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDate fechaApertura;

    @Column(name = "fecha_cierre")
    private LocalDate fechaCierre;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private EstadoIncidencia estado;
}
