package org.example.energy.service.serviceImpl;

import lombok.AllArgsConstructor;
import org.example.energy.dto.FacturaCreateDTO;
import org.example.energy.dto.FacturaResponseDTO;
import org.example.energy.entity.domain.Contrato;
import org.example.energy.entity.domain.Factura;
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

import java.util.List;

@Service
@AllArgsConstructor
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;
    private final ContratoRepository contratoRepository;
    private final FacturaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<FacturaResponseDTO> getAll(Pageable pageable) {
        Page<Factura> facturas = facturaRepository.findAll(pageable);
        return facturas.map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public FacturaResponseDTO getById(Integer id) {
        Factura factura = findById(id);
        return mapper.toDTO(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> getByContratoId(Integer id) {
        return mapper.toDTOList(facturaRepository.findByContratoContratoId(id));
    }

    @Override
    @Transactional
    public FacturaResponseDTO create(FacturaCreateDTO dto) {
        Contrato contrato = contratoRepository.findById(dto.contratoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Contrato no encontrado con el ID: " + dto.contratoId()
                ));

        Factura factura = mapper.toEntity(dto);
        factura.setContrato(contrato);

        Factura saved = facturaRepository.save(factura);
        return mapper.toDTO(saved);
    }

    @Override
    @Transactional
    public FacturaResponseDTO pagarFactura(Integer id) {

        Factura factura = findById(id);

        if (factura.getEstadoPago() == EstadoPago.PAGADA) {
            throw new BusinessRuleException(
                    ErrorCode.FACTURA_YA_PAGADA.name()

            );
        }

        if (factura.getEstadoPago() == EstadoPago.CANCELADA) {
            throw new BusinessRuleException(
                    ErrorCode.BUSINESS_RULE_VIOLATION.name()
            );
        }

        factura.setEstadoPago(EstadoPago.PAGADA);

        return mapper.toDTO(factura);
    }

    @Transactional
    @Override
    public FacturaResponseDTO cancelarFactura(Integer id) {
        Factura factura = findById(id);

        if (factura.getEstadoPago() == EstadoPago.PAGADA) {
            throw new BusinessRuleException(
                    ErrorCode.FACTURA_YA_PAGADA.name()
            );
        }

        if (factura.getEstadoPago() == EstadoPago.CANCELADA) {
            throw new BusinessRuleException(
                    ErrorCode.BUSINESS_RULE_VIOLATION.name()
            );
        }

        factura.setEstadoPago(EstadoPago.CANCELADA);

        return mapper.toDTO(factura);
    }

    @Override
    public void deleteById(Integer id) {
        Factura factura = findById(id);

        if (factura.getEstadoPago() == EstadoPago.PAGADA) {
            throw new BusinessRuleException(
                    ErrorCode.FACTURA_YA_PAGADA.name()
            );
        }
        facturaRepository.delete(factura);
    }

    @Override
    @Transactional
    public void generarFacturas(Integer mes) {
        if (mes == null || mes < 1 || mes > 12) {
            throw new BusinessRuleException(
                    ErrorCode.INVALID_INPUT.name()
            );
        }
        facturaRepository.generarFacturas(mes);
    }

    private Factura findById(Integer id){
        return facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Factura no encontrada con el ID: " + id
                ));
    }
}
