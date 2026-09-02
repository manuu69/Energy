package org.example.energy.entity.view;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.energy.enums.TipoIncidencia;

@Entity
@Getter
@Table(name = "vw_incidencias_criticas")
public class IncidenciaCriticaView {
    @Id
    @Column(name = "incidencia_id")
    private Integer incidenciaId;

    @Column(name = "contrato_id")
    private Integer contratoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoIncidencia tipo;

    @Column(name = "nombre")
    private String nombreCliente;

    @Column(name = "dias_abierta")
    private Integer diasAbierta;
}
