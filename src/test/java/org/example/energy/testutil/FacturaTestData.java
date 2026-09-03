package org.example.energy.testutil;

import org.example.energy.dto.factura.FacturaCreateDTO;
import org.example.energy.dto.factura.FacturaResponseDTO;
import org.example.energy.entity.domain.Contrato;
import org.example.energy.entity.domain.Factura;
import org.example.energy.enums.EstadoPago;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class FacturaTestData {

    private FacturaTestData() {
        // Evita que se instancie esta clase
    }

    public static FacturaResponseDTO crearFacturaResponseDTO() {
        return new FacturaResponseDTO(
                1,
                8,
                LocalDate.of(2024, 8, 5),
                new BigDecimal("100.00"),
                EstadoPago.PENDIENTE,
                LocalDate.of(2024, 8, 25),
                LocalDate.of(2024, 8, 25)
        );
    }

    public static Factura crearFactura() {
        Factura factura = new Factura();

        factura.setFacturaId(1);
        factura.setFechaEmision(LocalDate.of(2024, 8, 5));
        factura.setImporte(new BigDecimal("100.00"));
        factura.setEstadoPago(EstadoPago.PAGADA);
        factura.setFechaVencimiento(LocalDate.of(2024, 8, 25));

        return factura;
    }

    public static FacturaCreateDTO crearFacturaCreateDTO() {
        return new FacturaCreateDTO(
                8,
                LocalDate.of(2024, 8, 5),
                new BigDecimal("100.00"),
                EstadoPago.PENDIENTE,
                LocalDate.of(2024, 8, 25)
        );
    }

    public static Factura crearFacturaPendiente() {
        return crearFacturaConEstado(EstadoPago.PENDIENTE);
    }

    public static Factura crearFacturaPagada() {
        return crearFacturaConEstado(EstadoPago.PAGADA);
    }

    public static Factura crearFacturaCancelada() {
        return crearFacturaConEstado(EstadoPago.CANCELADA);
    }

    public static Factura crearFacturaConEstado(EstadoPago estadoPago) {
        Contrato contrato = new Contrato();
        contrato.setContratoId(8);

        Factura factura = new Factura();
        factura.setFacturaId(1);
        factura.setContrato(contrato);
        factura.setFechaEmision(LocalDate.of(2024, 8, 5));
        factura.setImporte(new BigDecimal("100.00"));
        factura.setEstadoPago(estadoPago);
        factura.setFechaVencimiento(LocalDate.of(2024, 8, 25));

        return factura;
    }

    public static FacturaResponseDTO crearFacturaResponseDTOPendiente() {
        return crearFacturaResponseDTOConEstado(EstadoPago.PENDIENTE);
    }

    public static FacturaResponseDTO crearFacturaResponseDTOPagada() {
        return crearFacturaResponseDTOConEstado(EstadoPago.PAGADA);
    }

    public static FacturaResponseDTO crearFacturaResponseDTOCancelada() {
        return crearFacturaResponseDTOConEstado(EstadoPago.CANCELADA);
    }

    public static FacturaResponseDTO crearFacturaResponseDTOConEstado(EstadoPago estadoPago) {
        return new FacturaResponseDTO(
                1,
                8,
                LocalDate.of(2024, 8, 5),
                new BigDecimal("100.00"),
                estadoPago,
                LocalDate.of(2024, 8, 25),
                LocalDate.of(2024, 8, 25)
        );
    }
}