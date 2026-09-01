package org.example.energy.repository.domain;

import org.example.energy.entity.domain.Lectura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface LecturaRepository extends JpaRepository<Lectura, Integer> {
    Page<Lectura> findByContratoContratoId(Integer id, Pageable pageable);

    @Modifying
    @Query(value = "CALL registrar_lectura(:contratoId, CAST(:fecha AS date), :consumoKwh, :tipoLectura)",
            nativeQuery = true)
    void registrarLectura(
            @Param("contratoId")  Integer contratoId,
            @Param("fecha") LocalDate fecha,
            @Param("consumoKwh") BigDecimal consumoKwh,
            @Param("tipoLectura") String tipoLectura
    );

    @Query(value = "select l.lectura_id, l.contrato_id, l.fecha, l.consumo_kwh, l.tipo_lectura " +
            "from vw_ultima_lectura l", nativeQuery = true)
    Optional<Lectura> findLastLecturaByContratoId(@Param("contratoId")  Integer contratoId);

}
