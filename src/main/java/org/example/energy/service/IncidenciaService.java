package org.example.energy.service;

import org.example.energy.dto.incidencia.IncidenciaCreateDTO;
import org.example.energy.dto.incidencia.IncidenciaCriticaDTO;
import org.example.energy.dto.incidencia.IncidenciaResponseDTO;
import org.example.energy.dto.incidencia.IncidenciaUpdateDTO;
import org.example.energy.enums.EstadoContrato;
import org.example.energy.enums.EstadoIncidencia;
import org.example.energy.enums.TipoIncidencia;
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
