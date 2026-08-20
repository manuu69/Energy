package org.example.energy.service.serviceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.FacturaCreateDTO;
import org.example.energy.dto.FacturaResponseDTO;
import org.example.energy.entity.domain.Contrato;
import org.example.energy.entity.domain.Factura;
import org.example.energy.enums.EstadoContrato;
import org.example.energy.enums.EstadoPago;
import org.example.energy.exception.BusinessRuleException;
import org.example.energy.exception.ErrorCode;
import org.example.energy.exception.ResourceNotFoundException;
import org.example.energy.mapper.FacturaMapper;
import org.example.energy.repository.domain.ContratoRepository;
import org.example.energy.repository.domain.FacturaRepository;
import org.example.energy.service.FacturaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;
    private final ContratoRepository contratoRepository;
    private final FacturaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<FacturaResponseDTO> getAll(Pageable pageable) {
        log.debug(
                "Consultando facturas paginadas. page={}, size={}, sort={}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<Factura> facturas = facturaRepository.findAll(pageable);

        log.info(
                "Consulta de facturas realizada. totalElements={}, totalPages={}, currentPage={}",
                facturas.getTotalElements(),
                facturas.getTotalPages(),
                facturas.getNumber()
        );

        return facturas.map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public FacturaResponseDTO getById(Integer id) {
        log.debug("Buscando factura con id={}", id);

        Factura factura = findById(id);
        return mapper.toDTO(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> getByContratoId(Integer id) {
        log.debug("Consultando facturas del contrato con id={}", id);

        List<Factura> facturas = facturaRepository.findByContratoContratoId(id);

        log.info(
                "Facturas encontradas para contrato id={}: total={}",
                id,
                facturas.size()
        );

        return mapper.toDTOList(facturas);
    }

    @Override
    @Transactional
    public FacturaResponseDTO create(FacturaCreateDTO dto) {
        log.info("Iniciando creación de factura para contrato id={}", dto.contratoId());

        Contrato contrato = contratoRepository.findById(dto.contratoId())
                .orElseThrow(() -> {
                    log.warn("No se encontró contrato con id={}", dto.contratoId());
                    return new ResourceNotFoundException(
                            "Contrato no encontrado con el ID: " + dto.contratoId()
                    );
                });

        log.debug(
                "Contrato encontrado. contratoId={}, estado={}",
                contrato.getContratoId(),
                contrato.getEstado()
        );

        if (contrato.getEstado() != EstadoContrato.ACTIVO) {
            log.warn(
                    "Creación de factura rechazada. contratoId={} estado={}",
                    dto.contratoId(),
                    contrato.getEstado()
            );

            throw new BusinessRuleException(
                    ErrorCode.CONTRATO_NO_ACTIVO.name()
            );
        }

        LocalDate fechaEmision = dto.fechaEmision();
        LocalDate inicioMes = fechaEmision.withDayOfMonth(1);
        LocalDate finMes = fechaEmision.withDayOfMonth(fechaEmision.lengthOfMonth());

        log.debug(
                "Comprobando duplicidad de factura. contratoId={}, inicioMes={}, finMes={}",
                dto.contratoId(),
                inicioMes,
                finMes
        );

        boolean yaExisteFactura = facturaRepository.existsByContratoContratoIdAndFechaEmisionBetween(
                dto.contratoId(),
                inicioMes,
                finMes
        );

        if (yaExisteFactura) {
            log.warn(
                    "Creación de factura rechazada. Ya existe factura para contratoId={} entre {} y {}",
                    dto.contratoId(),
                    inicioMes,
                    finMes
            );

            throw new BusinessRuleException(
                    ErrorCode.BUSINESS_RULE_VIOLATION.name()
            );
        }

        Factura factura = mapper.toEntity(dto);
        factura.setContrato(contrato);

        Factura saved = facturaRepository.save(factura);

        log.info(
                "Factura creada correctamente. facturaId={}, contratoId={}",
                saved.getFacturaId(),
                dto.contratoId()
        );

        return mapper.toDTO(saved);
    }

    @Override
    @Transactional
    public FacturaResponseDTO pagarFactura(Integer id) {
        log.info("Iniciando pago de factura id={}", id);

        Factura factura = findById(id);

        log.debug(
                "Factura encontrada para pago. facturaId={}, estadoActual={}",
                id,
                factura.getEstadoPago()
        );

        if (factura.getEstadoPago() == EstadoPago.PAGADA) {
            log.warn("Pago rechazado. La factura id={} ya está pagada", id);

            throw new BusinessRuleException(
                    ErrorCode.FACTURA_YA_PAGADA.name()
            );
        }

        if (factura.getEstadoPago() == EstadoPago.CANCELADA) {
            log.warn("Pago rechazado. La factura id={} está cancelada", id);

            throw new BusinessRuleException(
                    ErrorCode.BUSINESS_RULE_VIOLATION.name()
            );
        }

        factura.setEstadoPago(EstadoPago.PAGADA);

        log.info("Factura id={} marcada como PAGADA correctamente", id);

        return mapper.toDTO(factura);
    }

    @Override
    @Transactional
    public FacturaResponseDTO cancelarFactura(Integer id) {
        log.info("Iniciando cancelación de factura id={}", id);

        Factura factura = findById(id);

        log.debug(
                "Factura encontrada para cancelación. facturaId={}, estadoActual={}",
                id,
                factura.getEstadoPago()
        );

        if (factura.getEstadoPago() == EstadoPago.PAGADA) {
            log.warn("Cancelación rechazada. La factura id={} ya está pagada", id);

            throw new BusinessRuleException(
                    ErrorCode.FACTURA_YA_PAGADA.name()
            );
        }

        if (factura.getEstadoPago() == EstadoPago.CANCELADA) {
            log.warn("Cancelación rechazada. La factura id={} ya está cancelada", id);

            throw new BusinessRuleException(
                    ErrorCode.BUSINESS_RULE_VIOLATION.name()
            );
        }

        factura.setEstadoPago(EstadoPago.CANCELADA);

        log.info("Factura id={} marcada como CANCELADA correctamente", id);

        return mapper.toDTO(factura);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        log.info("Iniciando eliminación de factura id={}", id);

        Factura factura = findById(id);

        log.debug(
                "Factura encontrada para eliminación. facturaId={}, estadoActual={}",
                id,
                factura.getEstadoPago()
        );

        if (factura.getEstadoPago() == EstadoPago.PAGADA) {
            log.warn("Eliminación rechazada. La factura id={} ya está pagada", id);

            throw new BusinessRuleException(
                    ErrorCode.FACTURA_YA_PAGADA.name()
            );
        }

        facturaRepository.delete(factura);

        log.info("Factura id={} eliminada correctamente", id);
    }

    @Override
    @Transactional
    public void generarFacturas(Integer mes) {
        log.info("Iniciando generación masiva de facturas para mes={}", mes);

        if (mes == null || mes < 1 || mes > 12) {
            log.warn("Generación de facturas rechazada. Mes inválido={}", mes);

            throw new BusinessRuleException(
                    ErrorCode.INVALID_INPUT.name()
            );
        }

        facturaRepository.generarFacturas(mes);

        log.info("Procedimiento de generación de facturas ejecutado correctamente para mes={}", mes);
    }

    private Factura findById(Integer id) {

        return facturaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Factura no encontrada con id={}", id);

                    return new ResourceNotFoundException(
                            "Factura no encontrada con el ID: " + id
                    );
                });
    }
}