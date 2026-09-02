package org.example.energy.repository.view;

import org.example.energy.entity.view.IncidenciaCriticaView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidenciaCriticaRepository extends JpaRepository<IncidenciaCriticaView, Integer> {
    List<IncidenciaCriticaView> findByContratoId(Integer contratoId);
    List<IncidenciaCriticaView> findByDiasAbiertaGreaterThan(Integer dias);
}
