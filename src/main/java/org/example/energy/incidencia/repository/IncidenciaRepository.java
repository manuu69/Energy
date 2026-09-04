package org.example.energy.incidencia.repository;

import org.example.energy.incidencia.entity.Incidencia;
import org.example.energy.common.enums.EstadoIncidencia;
import org.example.energy.common.enums.TipoIncidencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Integer> {
    Page<Incidencia> findByContratoContratoId(Integer contratoId, Pageable pageable);

    Page<Incidencia> findByEstado(EstadoIncidencia estado, Pageable pageable);

    Page<Incidencia> findByTipo(TipoIncidencia tipo, Pageable pageable);
}
