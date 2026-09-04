package org.example.energy.contrato.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.energy.cliente.entity.Cliente;
import org.example.energy.common.enums.EstadoContrato;
import org.example.energy.common.enums.TipoTarifa;
import org.example.energy.zona.entity.Zona;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "tarifa", length = 10, nullable = false)
    private TipoTarifa tarifa;

    @Column(name = "potencia_kw", precision = 6, scale = 2)
    private BigDecimal potenciaKw;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoContrato estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zona_id")
    private Zona zona;
}
