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

    @Column(name = "facturacion_mes_actual")
    private BigDecimal facturacionMesActual;

    @Column(name = "facturacion_mes_anterior")
    private BigDecimal facturacionMesAnterior;

    @Column(name = "importe_cobrado_total")
    private BigDecimal importeCobradoTotal;

    @Column(name = "importe_cobrado_mes_actual")
    private BigDecimal importeCobradoMesActual;

    @Column(name = "facturas_vencidas")
    private Long facturasVencidas;

    @Column(name = "clientes_con_deuda")
    private Long clientesConDeuda;

    @Column(name = "importe_vencido")
    private BigDecimal importeVencido;

    @Column(name = "incidencias_criticas")
    private Long incidenciasCriticas;
}
