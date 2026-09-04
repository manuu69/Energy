package org.example.energy.factura.repository;

import org.example.energy.factura.entity.Factura;
import org.example.energy.common.enums.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
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
}
