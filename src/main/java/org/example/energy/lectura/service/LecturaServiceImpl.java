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
    public Page<LecturaResponseDTO> getAll(Pageable pageable) {
        Page<Lectura> lecturas = lecturaRepository.findAll(pageable);

        return lecturas.map(lecturaMapper::toDTO);
    }

    @Override
    public LecturaResponseDTO getById(Integer id) {
        Lectura lectura = findLecturaById(id);

        return lecturaMapper.toDTO(lectura);
    }

    @Override
    public Page<LecturaResponseDTO> getByContratoId(Integer id, Pageable pageable) {
        if (!contratoRepository.existsById(id)){
            throw new ResourceNotFoundException("Contrato no encontrado con el id: " + id);
        }

        Page<Lectura> lecturas = lecturaRepository.findByContratoContratoId(id, pageable);
        return lecturas.map(lecturaMapper::toDTO);
    }

    @Override
    @Transactional
    public LecturaResponseDTO create(LecturaCreateDTO dto) {
        log.info("Registrando lectura. contratoId={}, fecha={}",
                dto.contratoId(), dto.fecha());

        lecturaRepository.registrarLectura(
                dto.contratoId(),
                dto.fecha(),
                dto.consumoKwh(),
                dto.tipoLectura().toString()
        );

        return lecturaRepository
                .findLastLecturaByContratoId(dto.contratoId())
                .map(lecturaMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Error al recuperar la lectura registrada"
                ));
    }

    @Override
    @Transactional
    public LecturaResponseDTO update(LecturaUpdateDTO dto, Integer id) {
        Lectura lectura = findLecturaById(id);

        lecturaMapper.updateEntityFromDTO(dto, lectura);
        return lecturaMapper.toDTO(lectura);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!lecturaRepository.existsById(id)){
            throw new ResourceNotFoundException("Lectura no encontrada con el id: " + id);
        }
        lecturaRepository.deleteById(id);
    }

    @Override
    public List<LecturaAnalisisDTO> getAnalisis() {
        return lecturaAnalisisRepository.findAll()
                .stream()
                .map(lecturaMapper::toAnalisisDTO)
                .toList();
    }

    @Override
    public List<LecturaAnalisisDTO> getAnalisisByContrato(Integer contratoId) {
        return lecturaAnalisisRepository.findByContratoId(contratoId)
                .stream()
                .map(lecturaMapper::toAnalisisDTO)
                .toList();
    }

    @Override
    public List<LecturaAnalisisDTO> getAnomalias(BigDecimal umbral) {
        BigDecimal limiteDefault = umbral != null ? umbral : BigDecimal.valueOf(50);

        return lecturaAnalisisRepository.findAnomalias(limiteDefault)
                .stream()
                .map(lecturaMapper::toAnalisisDTO)
                .toList();
    }

    private @NonNull Lectura findLecturaById(Integer id){
        return lecturaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Lectura no encontrada con id={}", id);

                    return new ResourceNotFoundException(
                            "Lectura no encontrada con el ID: " + id
                    );
                });
    }
}
