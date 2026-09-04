package org.example.energy.contrato.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.contrato.dto.ContratoCreateDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<ContratoResponseDTO> getAll(Pageable pageable) {
        Page<Contrato> contratos = contratoRepository.findAll(pageable);

        return contratos.map(contratoMapper::toDTO);
    }

    @Override
    public ContratoResponseDTO getById(Integer id) {
        Contrato contrato = findById(id);
        return contratoMapper.toDTO(contrato);
    }

    @Override
    @Transactional
    public ContratoResponseDTO create(ContratoCreateDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente no existe con el id: " + dto.clienteId()
                        )
                );

        long cantContratosma = countContratosByCliente(dto.clienteId());

        if (cantContratosma >= 3) {
            throw new BusinessRuleException(
                    ErrorCode.LIMITE_CONTRATOS_ALCANZADO
            );
        }

        Contrato contrato = contratoMapper.toEntity(dto);
        contrato.setCliente(cliente);
        //contrato.setZona();
        contrato.setEstado(EstadoContrato.ACTIVO);

        Contrato savedContrato = contratoRepository.save(contrato);
        return contratoMapper.toDTO(savedContrato);
    }

    @Override
    @Transactional
    public ContratoResponseDTO update(Integer id, ContratoUpdateDTO dto) {
        Contrato contrato = findById(id);

        //PERMITIR CAMBIO DE ZONA, HAZLO MANUEL DEL FUTURO

        contratoMapper.updateEntityFromDTO(dto, contrato);
        return contratoMapper.toDTO(contrato);
    }

    @Override
    @Transactional
    public ContratoResponseDTO darBaja(Integer id) {
        Contrato contrato = findById(id);

        if (contrato.getEstado().equals(EstadoContrato.BAJA)){
            throw new BusinessRuleException(ErrorCode.CONTRATO_YA_DADO_DE_BAJA);
        }
        contrato.setEstado(EstadoContrato.BAJA);
        return contratoMapper.toDTO(contrato);
    }

    @Override
    @Transactional
    public ContratoResponseDTO suspender(Integer id) {
        Contrato contrato = findById(id);

        switch (contrato.getEstado()){
            case BAJA -> throw new BusinessRuleException(
                    ErrorCode.CONTRATO_YA_DADO_DE_BAJA);
            case SUSPENDIDO -> throw new BusinessRuleException(
                    ErrorCode.CONTRATO_YA_SUSPENDIDO);
            case ACTIVO -> contrato.setEstado(EstadoContrato.SUSPENDIDO);
            default -> throw new BusinessRuleException(
                    ErrorCode.ESTADO_CONTRATO_NO_VALIDO);
        }

        return contratoMapper.toDTO(contrato);
    }

    @Override
    @Transactional
    public ContratoResponseDTO activar(Integer id) {
        Contrato contrato = findById(id);

        switch (contrato.getEstado()){
            case BAJA -> throw new BusinessRuleException(
                    ErrorCode.CONTRATO_YA_DADO_DE_BAJA);
            case SUSPENDIDO -> contrato.setEstado(EstadoContrato.ACTIVO);
            case ACTIVO -> throw new BusinessRuleException(
                    ErrorCode.CONTRATO_YA_ACTIVO);
            default -> throw new BusinessRuleException(
                    ErrorCode.ESTADO_CONTRATO_NO_VALIDO);
        }

        return contratoMapper.toDTO(contrato);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        if (!contratoRepository.existsById(id)){
            throw new ResourceNotFoundException("Contato no encontrado con el id: " + id);
        }
        contratoRepository.deleteById(id);
    }

    private Contrato findById(Integer id){
        return contratoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Contrato no encontrado con id={}", id);

                    return new ResourceNotFoundException(
                            "Contrato no encontrado con el ID: " + id
                    );
                });
    }

    private long countContratosByCliente(Integer id){
        return contratoRepository.countByClienteId(id);
    }
}
