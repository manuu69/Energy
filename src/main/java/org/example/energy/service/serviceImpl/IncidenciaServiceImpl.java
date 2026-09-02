package org.example.energy.service.serviceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.incidencia.IncidenciaCreateDTO;
import org.example.energy.dto.incidencia.IncidenciaCriticaDTO;
import org.example.energy.dto.incidencia.IncidenciaResponseDTO;
import org.example.energy.dto.incidencia.IncidenciaUpdateDTO;
import org.example.energy.entity.domain.Contrato;
import org.example.energy.entity.domain.Incidencia;
import org.example.energy.enums.EstadoIncidencia;
import org.example.energy.enums.TipoIncidencia;
import org.example.energy.exception.code.ErrorCode;
import org.example.energy.exception.type.BusinessRuleException;
import org.example.energy.exception.type.ResourceNotFoundException;
import org.example.energy.mapper.IncidenciaMapper;
import org.example.energy.repository.domain.ContratoRepository;
import org.example.energy.repository.domain.IncidenciaRepository;
import org.example.energy.repository.view.IncidenciaCriticaRepository;
import org.example.energy.service.IncidenciaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class IncidenciaServiceImpl implements IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;
    private final IncidenciaCriticaRepository incidenciaCriticaRepository;
    private final ContratoRepository contratoRepository;
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
     * @return dto
     */
    @Override
    @Transactional
    public IncidenciaResponseDTO create(IncidenciaCreateDTO dto) {
        Contrato contrato = contratoRepository.findById(dto.contratoId())
                .orElseThrow(() ->
                    new ResourceNotFoundException("Contrato no encontrado con el id: " + dto.contratoId())
                );

        Incidencia incidencia = incidenciaMapper.toEntity(dto);

        incidencia.setContrato(contrato);
        incidencia.setEstado(EstadoIncidencia.ABIERTA);
        incidencia.setFechaApertura(LocalDate.now());
        incidencia.setFechaCierre(null);

        Incidencia saved = incidenciaRepository.save(incidencia);
        return incidenciaMapper.toDTO(saved);
    }

    /**
     * @param id
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public IncidenciaResponseDTO update(Integer id, IncidenciaUpdateDTO dto) {
        Incidencia incidencia = getIncidenciaById(id);

        incidenciaMapper.updateEntityFromDTO(dto, incidencia);
        return incidenciaMapper.toDTO(incidencia);
    }

    /**
     * @param id
     * @return
     */
    @Override
    @Transactional
    public IncidenciaResponseDTO iniciarGestion(Integer id) {
        log.info("Iniciando gestión de la incidencia id={}", id);

        Incidencia incidencia = getIncidenciaById(id);

        if (incidencia.getEstado() == EstadoIncidencia.EN_GESTION) {
            log.warn("La incidencia id={} ya se encuentra en gestión", id);

            throw new BusinessRuleException(
                    ErrorCode.INCIDENCIA_YA_EN_GESTION
            );
        }

        if (incidencia.getEstado() == EstadoIncidencia.CERRADA) {
            log.warn("No se puede iniciar la gestión de la incidencia id={} porque ya está cerrada", id);

            throw new BusinessRuleException(
                    ErrorCode.INCIDENCIA_YA_CERRADA
            );
        }

        incidencia.setEstado(EstadoIncidencia.EN_GESTION);

        log.info("Incidencia id={} puesta en gestión correctamente", id);

        return incidenciaMapper.toDTO(incidencia);
    }

    /**
     * @param id
     * @return
     */
    @Override
    @Transactional
    public IncidenciaResponseDTO cerrar(Integer id) {
        log.info("Iniciando gestión de cerrar de la incidencia id={}", id);

        Incidencia incidencia = getIncidenciaById(id);

        if (incidencia.getEstado() == EstadoIncidencia.CERRADA) {
            log.warn("No se puede cerrar la incidencia id={} porque ya está cerrada", id);

            throw new BusinessRuleException(
                    ErrorCode.INCIDENCIA_YA_CERRADA
            );
        }

        incidencia.setEstado(EstadoIncidencia.CERRADA);
        incidencia.setFechaCierre(LocalDate.now());

        log.info("Incidencia id={} cerrada correctamente", id);

        return incidenciaMapper.toDTO(incidencia);
    }

    /**
     * @return
     */
    @Override
    public List<IncidenciaCriticaDTO> getIncidenciasCriticas() {
        return incidenciaCriticaRepository
                .findAll()
                .stream().map(incidenciaMapper::toCriticaDTO)
                .toList();
    }

    /**
     * @param contratoId
     * @return
     */
    @Override
    public List<IncidenciaCriticaDTO> getIncidenciasCriticasByContrato(Integer contratoId) {
        if (!contratoRepository.existsById(contratoId)){
            throw new ResourceNotFoundException("Contrato no encontrado con el id: " + contratoId);
        }

        return incidenciaCriticaRepository.findByContratoId(contratoId)
                .stream().map(incidenciaMapper::toCriticaDTO)
                .toList();
    }

    /**
     * @param id
     * @return
     */
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
