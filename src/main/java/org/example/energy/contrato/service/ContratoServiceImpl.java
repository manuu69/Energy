package org.example.energy.contrato.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.contrato.dto.ContratoCreateDTO;
import org.example.energy.contrato.dto.ContratoFilter;
import org.example.energy.contrato.dto.ContratoResponseDTO;
import org.example.energy.contrato.dto.ContratoUpdateDTO;
import org.example.energy.cliente.entity.Cliente;
import org.example.energy.contrato.entity.Contrato;
import org.example.energy.common.enums.EstadoContrato;
import org.example.energy.common.exception.code.ErrorCode;
import org.example.energy.common.exception.type.BusinessRuleException;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.contrato.mapper.ContratoMapper;
import org.example.energy.cliente.repository.ClienteRepository;
import org.example.energy.contrato.repository.ContratoRepository;
import org.example.energy.contrato.spec.ContratoSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ContratoServiceImpl implements ContratoService {

    private final ContratoRepository contratoRepository;
    private final ClienteRepository clienteRepository;
    private final ContratoMapper contratoMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ContratoResponseDTO> getAll(ContratoFilter filter, Pageable pageable) {
        log.debug(
                "Consultando contratos paginados. page={}, size={}, sort={}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
        Specification<Contrato> spec = ContratoSpecifications.conFiltros(filter);

        Page<Contrato> contratos = contratoRepository.findAll(spec, pageable);

        log.info(
                "Consulta de contratos realizada. totalElements={}, totalPages={}, currentPage={}",
                contratos.getTotalElements(),
                contratos.getTotalPages(),
                contratos.getNumber()
        );

        return contratos.map(contratoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ContratoResponseDTO getById(Integer id) {
        log.debug("Buscando contrato con id={}", id);

        Contrato contrato = findById(id);
        return contratoMapper.toDTO(contrato);
    }

    @Override
    @Transactional
    public ContratoResponseDTO create(ContratoCreateDTO dto) {
        log.info("Iniciando creación de contrato para cliente id={}", dto.clienteId());

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> {
                    log.warn("No se encontró cliente con id={}", dto.clienteId());
                    return new ResourceNotFoundException(
                            "Cliente no existe con el id: " + dto.clienteId()
                    );
                });

        long cantContratos = countContratosByCliente(dto.clienteId());

        log.debug(
                "Verificando límite de contratos del cliente. clienteId={}, contratosActuales={}",
                dto.clienteId(),
                cantContratos
        );

        if (cantContratos >= 3) {
            log.warn(
                    "Creación de contrato rechazada. El cliente id={} ya alcanzó el límite de 3 contratos",
                    dto.clienteId()
            );

            throw new BusinessRuleException(
                    ErrorCode.LIMITE_CONTRATOS_ALCANZADO
            );
        }

        Contrato contrato = contratoMapper.toEntity(dto);
        contrato.setCliente(cliente);
        contrato.setEstado(EstadoContrato.ACTIVO);

        Contrato savedContrato = contratoRepository.save(contrato);

        log.info(
                "Contrato creado correctamente. contratoId={}, clienteId={}",
                savedContrato.getContratoId(),
                dto.clienteId()
        );

        return contratoMapper.toDTO(savedContrato);
    }

    @Override
    @Transactional
    public ContratoResponseDTO update(Integer id, ContratoUpdateDTO dto) {
        log.info("Iniciando actualización de contrato id={}", id);

        Contrato contrato = findById(id);

        log.debug(
                "Contrato encontrado para actualización. contratoId={}, estadoActual={}",
                id,
                contrato.getEstado()
        );

        contratoMapper.updateEntityFromDTO(dto, contrato);

        log.info("Contrato id={} actualizado correctamente", id);

        return contratoMapper.toDTO(contrato);
    }

    @Override
    @Transactional
    public ContratoResponseDTO darBaja(Integer id) {
        log.info("Iniciando baja de contrato id={}", id);

        Contrato contrato = findById(id);

        log.debug(
                "Contrato encontrado para baja. contratoId={}, estadoActual={}",
                id,
                contrato.getEstado()
        );

        if (contrato.getEstado().equals(EstadoContrato.BAJA)) {
            log.warn("Baja rechazada. El contrato id={} ya está dado de baja", id);

            throw new BusinessRuleException(ErrorCode.CONTRATO_YA_DADO_DE_BAJA);
        }

        contrato.setEstado(EstadoContrato.BAJA);

        log.info("Contrato id={} dado de BAJA correctamente", id);

        return contratoMapper.toDTO(contrato);
    }

    @Override
    @Transactional
    public ContratoResponseDTO suspender(Integer id) {
        log.info("Iniciando suspensión de contrato id={}", id);

        Contrato contrato = findById(id);

        log.debug(
                "Contrato encontrado para suspensión. contratoId={}, estadoActual={}",
                id,
                contrato.getEstado()
        );

        switch (contrato.getEstado()) {
            case BAJA -> {
                log.warn("Suspensión rechazada. El contrato id={} está en BAJA", id);
                throw new BusinessRuleException(ErrorCode.CONTRATO_YA_DADO_DE_BAJA);
            }
            case SUSPENDIDO -> {
                log.warn("Suspensión rechazada. El contrato id={} ya está SUSPENDIDO", id);
                throw new BusinessRuleException(ErrorCode.CONTRATO_YA_SUSPENDIDO);
            }
            case ACTIVO -> contrato.setEstado(EstadoContrato.SUSPENDIDO);
            default -> {
                log.warn("Suspensión rechazada. Estado no válido={}", contrato.getEstado());
                throw new BusinessRuleException(ErrorCode.ESTADO_CONTRATO_NO_VALIDO);
            }
        }

        log.info("Contrato id={} marcado como SUSPENDIDO correctamente", id);

        return contratoMapper.toDTO(contrato);
    }

    @Override
    @Transactional
    public ContratoResponseDTO activar(Integer id) {
        log.info("Iniciando activación de contrato id={}", id);

        Contrato contrato = findById(id);

        log.debug(
                "Contrato encontrado para activación. contratoId={}, estadoActual={}",
                id,
                contrato.getEstado()
        );

        switch (contrato.getEstado()) {
            case BAJA -> {
                log.warn("Activación rechazada. El contrato id={} está en BAJA", id);
                throw new BusinessRuleException(ErrorCode.CONTRATO_YA_DADO_DE_BAJA);
            }
            case SUSPENDIDO -> contrato.setEstado(EstadoContrato.ACTIVO);
            case ACTIVO -> {
                log.warn("Activación rechazada. El contrato id={} ya está ACTIVO", id);
                throw new BusinessRuleException(ErrorCode.CONTRATO_YA_ACTIVO);
            }
            default -> {
                log.warn("Activación rechazada. Estado no válido={}", contrato.getEstado());
                throw new BusinessRuleException(ErrorCode.ESTADO_CONTRATO_NO_VALIDO);
            }
        }

        log.info("Contrato id={} activado correctamente", id);

        return contratoMapper.toDTO(contrato);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        log.info("Iniciando eliminación de contrato id={}", id);

        if (!contratoRepository.existsById(id)) {
            log.warn("Eliminación rechazada. Contrato no encontrado con id={}", id);
            throw new ResourceNotFoundException("Contrato no encontrado con el id: " + id);
        }

        contratoRepository.deleteById(id);

        log.info("Contrato id={} eliminado correctamente", id);
    }

    private Contrato findById(Integer id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Contrato no encontrado con id={}", id);

                    return new ResourceNotFoundException(
                            "Contrato no encontrado con el ID: " + id
                    );
                });
    }

    private long countContratosByCliente(Integer id) {
        return contratoRepository.countByClienteId(id);
    }
}