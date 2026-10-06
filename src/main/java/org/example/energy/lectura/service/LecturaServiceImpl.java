package org.example.energy.lectura.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.lectura.dto.LecturaAnalisisDTO;
import org.example.energy.lectura.dto.LecturaCreateDTO;
import org.example.energy.lectura.dto.LecturaResponseDTO;
import org.example.energy.lectura.dto.LecturaUpdateDTO;
import org.example.energy.lectura.entity.Lectura;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.lectura.mapper.LecturaMapper;
import org.example.energy.contrato.repository.ContratoRepository;
import org.example.energy.lectura.repository.LecturaRepository;
import org.example.energy.lectura.repository.LecturaAnalisisRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class LecturaServiceImpl implements LecturaService {

    private final LecturaRepository lecturaRepository;
    private final LecturaAnalisisRepository lecturaAnalisisRepository;
    private final ContratoRepository contratoRepository;
    private final LecturaMapper lecturaMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<LecturaResponseDTO> getAll(Pageable pageable) {
        log.debug(
                "Consultando lecturas paginadas. page={}, size={}, sort={}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<Lectura> lecturas = lecturaRepository.findAll(pageable);

        log.info(
                "Consulta de lecturas realizada. totalElements={}, totalPages={}, currentPage={}",
                lecturas.getTotalElements(),
                lecturas.getTotalPages(),
                lecturas.getNumber()
        );

        return lecturas.map(lecturaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public LecturaResponseDTO getById(Integer id) {
        log.debug("Buscando lectura con id={}", id);

        Lectura lectura = findLecturaById(id);
        return lecturaMapper.toDTO(lectura);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LecturaResponseDTO> getByContratoId(Integer id, Pageable pageable) {
        log.debug(
                "Consultando lecturas para el contrato id={}. page={}, size={}",
                id,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        if (!contratoRepository.existsById(id)) {
            log.warn("Consulta rechazada. Contrato no encontrado con id={}", id);
            throw new ResourceNotFoundException("Contrato no encontrado con el id: " + id);
        }

        Page<Lectura> lecturas = lecturaRepository.findByContratoContratoId(id, pageable);

        log.info(
                "Consulta de lecturas por contrato realizada. contratoId={}, totalElements={}, totalPages={}",
                id,
                lecturas.getTotalElements(),
                lecturas.getTotalPages()
        );

        return lecturas.map(lecturaMapper::toDTO);
    }

    @Override
    @Transactional
    public LecturaResponseDTO create(LecturaCreateDTO dto) {
        log.info(
                "Iniciando registro de lectura. contratoId={}, fecha={}, tipoLectura={}",
                dto.contratoId(),
                dto.fecha(),
                dto.tipoLectura()
        );

        lecturaRepository.registrarLectura(
                dto.contratoId(),
                dto.fecha(),
                dto.consumoKwh(),
                dto.tipoLectura().toString()
        );

        LecturaResponseDTO responseDTO = lecturaRepository
                .findLastLecturaByContratoId(dto.contratoId())
                .map(lecturaMapper::toDTO)
                .orElseThrow(() -> {
                    log.warn("Error al recuperar la lectura recién registrada para contratoId={}", dto.contratoId());
                    return new ResourceNotFoundException("Error al recuperar la lectura registrada");
                });

        log.info(
                "Lectura registrada y recuperada correctamente. lecturaId={}, contratoId={}",
                responseDTO.lecturaId(),
                dto.contratoId()
        );

        return responseDTO;
    }

    @Override
    @Transactional
    public LecturaResponseDTO update(LecturaUpdateDTO dto, Integer id) {
        log.info("Iniciando actualización de lectura id={}", id);

        Lectura lectura = findLecturaById(id);

        log.debug("Lectura encontrada para actualización. lecturaId={}", id);

        lecturaMapper.updateEntityFromDTO(dto, lectura);

        log.info("Lectura id={} actualizada correctamente", id);

        return lecturaMapper.toDTO(lectura);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Iniciando eliminación de lectura id={}", id);

        if (!lecturaRepository.existsById(id)) {
            log.warn("Eliminación rechazada. Lectura no encontrada con id={}", id);
            throw new ResourceNotFoundException("Lectura no encontrada con el id: " + id);
        }

        lecturaRepository.deleteById(id);

        log.info("Lectura id={} eliminada correctamente", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LecturaAnalisisDTO> getAnalisis() {
        log.debug("Consultando análisis de todas las lecturas");

        List<LecturaAnalisisDTO> analisis = lecturaAnalisisRepository.findAll()
                .stream()
                .map(lecturaMapper::toAnalisisDTO)
                .toList();

        log.info("Consulta de análisis de lecturas realizada. Total registros={}", analisis.size());

        return analisis;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LecturaAnalisisDTO> getAnalisisByContrato(Integer contratoId) {
        log.debug("Consultando análisis de lecturas para el contrato id={}", contratoId);

        List<LecturaAnalisisDTO> analisis = lecturaAnalisisRepository.findByContratoId(contratoId)
                .stream()
                .map(lecturaMapper::toAnalisisDTO)
                .toList();

        log.info(
                "Consulta de análisis de lecturas por contrato realizada. contratoId={}, totalRegistros={}",
                contratoId,
                analisis.size()
        );

        return analisis;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LecturaAnalisisDTO> getAnomalias(BigDecimal umbral) {
        BigDecimal limiteDefault = umbral != null ? umbral : BigDecimal.valueOf(50);

        log.debug("Consultando anomalías de lecturas con umbral={}", limiteDefault);

        List<LecturaAnalisisDTO> anomalias = lecturaAnalisisRepository.findAnomalias(limiteDefault)
                .stream()
                .map(lecturaMapper::toAnalisisDTO)
                .toList();

        log.info(
                "Consulta de anomalías realizada con umbral={}. Anomalías detectadas={}",
                limiteDefault,
                anomalias.size()
        );

        return anomalias;
    }

    private @NonNull Lectura findLecturaById(Integer id) {
        return lecturaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Lectura no encontrada con id={}", id);
                    return new ResourceNotFoundException(
                            "Lectura no encontrada con el ID: " + id
                    );
                });
    }
}