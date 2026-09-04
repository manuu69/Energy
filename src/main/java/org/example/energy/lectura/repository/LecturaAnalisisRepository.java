package org.example.energy.lectura.repository;

import org.example.energy.lectura.entity.LecturaAnalisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LecturaAnalisisRepository extends JpaRepository<LecturaAnalisis, Integer> {

    List<LecturaAnalisis> findByContratoId(Integer contratoId);

    // Lecturas anómalas — diferencia positiva grande
    @Query("SELECT l FROM LecturaAnalisis l WHERE l.diferenciaConMedia > :umbral")
    List<LecturaAnalisis> findAnomalias(@Param("umbral") BigDecimal umbral);
}
