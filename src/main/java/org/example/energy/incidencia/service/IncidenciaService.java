package org.example.energy.incidencia.service;

import org.example.energy.incidencia.dto.IncidenciaCreateDTO;
import org.example.energy.incidencia.dto.IncidenciaCriticaDTO;
import org.example.energy.incidencia.dto.IncidenciaResponseDTO;
import org.example.energy.incidencia.dto.IncidenciaUpdateDTO;
import org.example.energy.common.enums.EstadoIncidencia;
import org.example.energy.common.enums.TipoIncidencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IncidenciaService {
    Page<IncidenciaResponseDTO> getAll(Pageable pageable);
    Page<IncidenciaResponseDTO> getByContratoId(Integer id, Pageable pageable);
    Page<IncidenciaResponseDTO> getByEstado(EstadoIncidencia estado, Pageable pageable);
    Page<IncidenciaResponseDTO> getByTipo(TipoIncidencia tipo, Pageable pageable);
    IncidenciaResponseDTO getById(Integer id);
    IncidenciaResponseDTO create(IncidenciaCreateDTO dto);
    IncidenciaResponseDTO update(Integer id, IncidenciaUpdateDTO dto);
    IncidenciaResponseDTO iniciarGestion(Integer id);
    IncidenciaResponseDTO cerrar(Integer id);
    List<IncidenciaCriticaDTO> getIncidenciasCriticas();
    List<IncidenciaCriticaDTO> getIncidenciasCriticasByContrato(Integer contratoId);

}
