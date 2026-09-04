package org.example.energy.incidencia.repository;

import org.example.energy.incidencia.entity.IncidenciaCriticaView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidenciaCriticaRepository extends JpaRepository<IncidenciaCriticaView, Integer> {
    List<IncidenciaCriticaView> findByContratoId(Integer contratoId);
    List<IncidenciaCriticaView> findByDiasAbiertaGreaterThan(Integer dias);
}
