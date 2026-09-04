package org.example.energy.factura.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Immutable
@Table(name = "vw_facturas_pendientes")
@Getter
public class FacturaPendienteView {

    @Id
    @Column(name = "factura_id")
    private Integer facturaId;

    @Column(name = "contrato_id")
    private Integer contratoId;

    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;

    private BigDecimal importe;

    @Column(name = "estado_pago")
    private String estadoPago;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;
}
