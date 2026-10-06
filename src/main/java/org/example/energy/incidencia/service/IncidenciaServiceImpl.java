package org.example.energy.incidencia.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.incidencia.dto.IncidenciaCreateDTO;
import org.example.energy.incidencia.dto.IncidenciaCriticaDTO;
import org.example.energy.incidencia.dto.IncidenciaResponseDTO;
import org.example.energy.incidencia.dto.IncidenciaUpdateDTO;
import org.example.energy.contrato.entity.Contrato;
import org.example.energy.incidencia.entity.Incidencia;
import org.example.energy.common.enums.EstadoIncidencia;
import org.example.energy.common.enums.TipoIncidencia;
import org.example.energy.common.exception.code.ErrorCode;
import org.example.energy.common.exception.type.BusinessRuleException;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.incidencia.mapper.IncidenciaMapper;
import org.example.energy.contrato.repository.ContratoRepository;
import org.example.energy.incidencia.repository.IncidenciaRepository;
import org.example.energy.incidencia.repository.IncidenciaCriticaRepository;
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

    @Override
    @Transactional(readOnly = true)
    public Page<IncidenciaResponseDTO> getAll(Pageable pageable) {
        log.debug(
                "Consultando incidencias paginadas. page={}, size={}, sort={}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<Incidencia> incidencias = incidenciaRepository.findAll(pageable);

        log.info(
                "Consulta de incidencias realizada. totalElements={}, totalPages={}, currentPage={}",
                incidencias.getTotalElements(),
                incidencias.getTotalPages(),
                incidencias.getNumber()
        );

        return incidencias.map(incidenciaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IncidenciaResponseDTO> getByContratoId(Integer contratoId, Pageable pageable) {
        log.debug(
                "Consultando incidencias para el contrato id={}. page={}, size={}",
                contratoId,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Incidencia> incidencias = incidenciaRepository.findByContratoContratoId(contratoId, pageable);

        log.info(
                "Consulta de incidencias por contrato realizada. contratoId={}, totalElements={}, totalPages={}",
                contratoId,
                incidencias.getTotalElements(),
                incidencias.getTotalPages()
        );

        return incidencias.map(incidenciaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IncidenciaResponseDTO> getByEstado(EstadoIncidencia estado, Pageable pageable) {
        log.debug(
                "Consultando incidencias por estado={}. page={}, size={}",
                estado,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Incidencia> incidencias = incidenciaRepository.findByEstado(estado, pageable);

        log.info(
                "Consulta de incidencias por estado realizada. estado={}, totalElements={}, totalPages={}",
                estado,
                incidencias.getTotalElements(),
                incidencias.getTotalPages()
        );

        return incidencias.map(incidenciaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IncidenciaResponseDTO> getByTipo(TipoIncidencia tipo, Pageable pageable) {
        log.debug(
                "Consultando incidencias por tipo={}. page={}, size={}",
                tipo,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Incidencia> incidencias = incidenciaRepository.findByTipo(tipo, pageable);

        log.info(
                "Consulta de incidencias por tipo realizada. tipo={}, totalElements={}, totalPages={}",
                tipo,
                incidencias.getTotalElements(),
                incidencias.getTotalPages()
        );

        return incidencias.map(incidenciaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public IncidenciaResponseDTO getById(Integer id) {
        log.debug("Buscando incidencia con id={}", id);

        Incidencia incidencia = getIncidenciaById(id);
        return incidenciaMapper.toDTO(incidencia);
    }

    @Override
    @Transactional
    public IncidenciaResponseDTO create(IncidenciaCreateDTO dto) {
        log.info("Iniciando creación de incidencia para contrato id={}", dto.contratoId());

        Contrato contrato = contratoRepository.findById(dto.contratoId())
                .orElseThrow(() -> {
                    log.warn("No se encontró contrato con id={}", dto.contratoId());
                    return new ResourceNotFoundException("Contrato no encontrado con el id: " + dto.contratoId());
                });

        Incidencia incidencia = incidenciaMapper.toEntity(dto);

        incidencia.setContrato(contrato);
        incidencia.setEstado(EstadoIncidencia.ABIERTA);
        incidencia.setFechaApertura(LocalDate.now());
        incidencia.setFechaCierre(null);

        Incidencia saved = incidenciaRepository.save(incidencia);

        log.info(
                "Incidencia creada correctamente. incidenciaId={}, contratoId={}",
                saved.getIncidenciaId(),
                dto.contratoId()
        );

        return incidenciaMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public IncidenciaResponseDTO update(Integer id, IncidenciaUpdateDTO dto) {
        log.info("Iniciando actualización de incidencia id={}", id);

        Incidencia incidencia = getIncidenciaById(id);

        log.debug("Incidencia encontrada para actualización. incidenciaId={}", id);

        incidenciaMapper.updateEntityFromDTO(dto, incidencia);

        log.info("Incidencia id={} actualizada correctamente", id);

        return incidenciaMapper.toDTO(incidencia);
    }

    @Override
    @Transactional
    public IncidenciaResponseDTO iniciarGestion(Integer id) {
        log.info("Iniciando gestión de la incidencia id={}", id);

        Incidencia incidencia = getIncidenciaById(id);

        log.debug(
                "Incidencia encontrada para puesta en gestión. incidenciaId={}, estadoActual={}",
                id,
                incidencia.getEstado()
        );

        if (incidencia.getEstado() == EstadoIncidencia.EN_GESTION) {
            log.warn("Puesta en gestión rechazada. La incidencia id={} ya se encuentra en gestión", id);
            throw new BusinessRuleException(ErrorCode.INCIDENCIA_YA_EN_GESTION);
        }

        if (incidencia.getEstado() == EstadoIncidencia.CERRADA) {
            log.warn("Puesta en gestión rechazada. La incidencia id={} ya está cerrada", id);
            throw new BusinessRuleException(ErrorCode.INCIDENCIA_YA_CERRADA);
        }

        incidencia.setEstado(EstadoIncidencia.EN_GESTION);

        log.info("Incidencia id={} puesta en gestión correctamente", id);

        return incidenciaMapper.toDTO(incidencia);
    }

    @Override
    @Transactional
    public IncidenciaResponseDTO cerrar(Integer id) {
        log.info("Iniciando proceso de cierre de la incidencia id={}", id);

        Incidencia incidencia = getIncidenciaById(id);

        log.debug(
                "Incidencia encontrada para cierre. incidenciaId={}, estadoActual={}",
                id,
                incidencia.getEstado()
        );

        if (incidencia.getEstado() == EstadoIncidencia.CERRADA) {
            log.warn("Cierre rechazado. La incidencia id={} ya está cerrada", id);
            throw new BusinessRuleException(ErrorCode.INCIDENCIA_YA_CERRADA);
        }

        incidencia.setEstado(EstadoIncidencia.CERRADA);
        incidencia.setFechaCierre(LocalDate.now());

        log.info("Incidencia id={} cerrada correctamente", id);

        return incidenciaMapper.toDTO(incidencia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidenciaCriticaDTO> getIncidenciasCriticas() {
        log.debug("Consultando todas las incidencias críticas");

        List<IncidenciaCriticaDTO> criticas = incidenciaCriticaRepository
                .findAll()
                .stream()
                .map(incidenciaMapper::toCriticaDTO)
                .toList();

        log.info("Consulta de incidencias críticas realizada. Total encontradas={}", criticas.size());

        return criticas;
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidenciaCriticaDTO> getIncidenciasCriticasByContrato(Integer contratoId) {
        log.debug("Consultando incidencias críticas para contrato id={}", contratoId);

        if (!contratoRepository.existsById(contratoId)) {
            log.warn("Consulta de críticas rechazada. No existe contrato con id={}", contratoId);
            throw new ResourceNotFoundException("Contrato no encontrado con el id: " + contratoId);
        }

        List<IncidenciaCriticaDTO> criticas = incidenciaCriticaRepository.findByContratoId(contratoId)
                .stream()
                .map(incidenciaMapper::toCriticaDTO)
                .toList();

        log.info(
                "Consulta de incidencias críticas por contrato realizada. contratoId={}, totalEncontradas={}",
                contratoId,
                criticas.size()
        );

        return criticas;
    }

    private Incidencia getIncidenciaById(Integer id) {
        return incidenciaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Incidencia no encontrada con id={}", id);
                    return new ResourceNotFoundException(
                            "Incidencia no encontrada con el ID: " + id
                    );
                });
    }
}