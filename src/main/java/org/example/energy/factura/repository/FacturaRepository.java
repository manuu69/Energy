package org.example.energy.factura.repository;

import jakarta.persistence.QueryHint;
import org.example.energy.contrato.entity.Contrato;
import org.example.energy.factura.dto.FacturaExportDTO;
import org.example.energy.factura.entity.Factura;
import org.example.energy.common.enums.EstadoPago;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.hibernate.jpa.HibernateHints.HINT_FETCH_SIZE;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer>, JpaSpecificationExecutor<Factura> {
    List<Factura> findByContratoContratoId(Integer contratoId);

    @Modifying
    @Query(value = "CALL generar_facturas(:mes)", nativeQuery = true)
    void generarFacturas(@Param("mes") Integer mes);

    boolean existsByContratoContratoIdAndFechaEmisionBetween(
        Integer contratoId,
        LocalDate inicioMes,
        LocalDate finMes
    );

    @Modifying
    @Query("""
    UPDATE Factura f
    SET f.estadoPago = :estadoVencida
    WHERE f.estadoPago = :estadoPendiente
      AND f.fechaVencimiento < :fechaActual
    """)
    int marcarFacturasVencidas(
            @Param("fechaActual") LocalDate fechaActual,
            @Param("estadoPendiente") EstadoPago estadoPendiente,
            @Param("estadoVencida") EstadoPago estadoVencida
    );

    @Query(value = """
    SELECT
        f.factura_id,
        co.contrato_id,
        c.cliente_id,
        c.nombre,
        f.fecha_emision,
        f.importe,
        f.estado_pago,
        f.fecha_vencimiento
    FROM facturas f
    JOIN contratos co ON f.contrato_id = co.contrato_id
    JOIN clientes c   ON co.cliente_id = c.cliente_id
    """,
            nativeQuery = true)
    Stream<FacturaExportDTO> streamAllForExport();
}
