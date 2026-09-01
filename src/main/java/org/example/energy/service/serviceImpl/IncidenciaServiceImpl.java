package org.example.energy.service.serviceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.incidencia.IncidenciaCreateDTO;
import org.example.energy.dto.incidencia.IncidenciaResponseDTO;
import org.example.energy.entity.domain.Incidencia;
import org.example.energy.enums.EstadoIncidencia;
import org.example.energy.enums.TipoIncidencia;
import org.example.energy.exception.type.ResourceNotFoundException;
import org.example.energy.mapper.IncidenciaMapper;
import org.example.energy.repository.domain.IncidenciaRepository;
import org.example.energy.service.IncidenciaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class IncidenciaServiceImpl implements IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;
    private final IncidenciaMapper incidenciaMapper;


    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<IncidenciaResponseDTO> getAll(Pageable pageable) {
        Page<Incidencia> incidencias = incidenciaRepository.findAll(pageable);

        return incidencias.map(incidenciaMapper::toDTO);
    }

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<IncidenciaResponseDTO> getByContratoId(Integer contratoId, Pageable pageable) {
        Page<Incidencia> incidencias = incidenciaRepository.findByContratoContratoId(contratoId, pageable);

        return incidencias.map(incidenciaMapper::toDTO);
    }

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<IncidenciaResponseDTO> getByEstado(EstadoIncidencia estado, Pageable pageable) {
        Page<Incidencia> incidencias = incidenciaRepository.findByEstado(estado, pageable);

        return incidencias.map(incidenciaMapper::toDTO);
    }

    /**
     * @param pageable
     * @return
     */
    @Override
    public Page<IncidenciaResponseDTO> getByTipo(TipoIncidencia tipo, Pageable pageable) {
        Page<Incidencia> incidencias = incidenciaRepository.findByTipo(tipo, pageable);

        return incidencias.map(incidenciaMapper::toDTO);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public IncidenciaResponseDTO getById(Integer id) {
        Incidencia incidencia = getIncidenciaById(id);
        return incidenciaMapper.toDTO(incidencia);
    }

    /**
     * @param dto
     * @return
     */
    @Override
    public IncidenciaResponseDTO create(IncidenciaCreateDTO dto) {
        return null;
    }

    /**
     * @param id
     * @param dto
     * @return
     */
    @Override
    public IncidenciaResponseDTO update(Integer id, IncidenciaCreateDTO dto) {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public IncidenciaResponseDTO iniciarGestion(Integer id) {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public IncidenciaResponseDTO cerrrar(Integer id) {
        return null;
    }

    private Incidencia getIncidenciaById(Integer id){
        return incidenciaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Incidencia no encontrada con id={}", id);

                    return new ResourceNotFoundException(
                            "Incidencia no encontrada con el ID: " + id
                    );
                });
    }
}
