package org.example.energy.repository.domain;

import org.example.energy.entity.domain.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, Integer> {
    @Query("SELECT z FROM Zona z WHERE z.padre.zonaId = :zonaId")
    List<Zona> findByPadreId(@Param("zonaId") Integer zonaId);
}
