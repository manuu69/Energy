package org.example.energy.entity.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.energy.enums.EstadoPago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "facturas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "factura_id")
    private Integer facturaId;

    // Relación con Contrato
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "importe", nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false)
    private EstadoPago estadoPago;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;
}
