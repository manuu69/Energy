package org.example.energy.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Entity
@Table(name = "vw_resumen_facturacion_cliente")
@Getter
@Immutable
public class ResumenFacturacionClienteView {

    @Id
    @Column(name = "cliente_id")
    private Integer clienteId;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "cantidad_contratos_activos")
    private Long cantidadContratosActivos;

    @Column(name = "total_facturado")
    private BigDecimal totalFacturado;

    @Column(name = "facturas_pendientes")
    private Long facturasPendientes;
}
