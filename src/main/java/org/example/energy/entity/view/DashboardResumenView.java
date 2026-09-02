package org.example.energy.entity.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Entity
@Immutable
@Getter
@Table(name = "vw_dashboard_resumen")
@NoArgsConstructor
public class DashboardResumenView {

    @Id
    @Column(name = "clientes_activos")
    private Long clientesActivos;

    @Column(name = "contratos_activos")
    private Long contratosActivos;

    @Column(name = "facturas_pendientes")
    private Long facturasPendientes;

    @Column(name = "importe_pendiente")
    private BigDecimal importePendiente;

    @Column(name = "incidencias_abiertas")
    private Long incidenciasAbiertas;

    @Column(name = "incidencias_en_gestion")
    private Long incidenciasEnGestion;

    @Column(name = "consumo_mes_actual")
    private BigDecimal consumoMesActual;
}
