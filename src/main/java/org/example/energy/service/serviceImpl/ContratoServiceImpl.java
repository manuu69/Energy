package org.example.energy.service.serviceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.contrato.ContratoCreateDTO;
import org.example.energy.dto.contrato.ContratoResponseDTO;
import org.example.energy.dto.contrato.ContratoUpdateDTO;
import org.example.energy.entity.domain.Cliente;
import org.example.energy.entity.domain.Contrato;
import org.example.energy.enums.EstadoContrato;
import org.example.energy.exception.code.ErrorCode;
import org.example.energy.exception.type.BusinessRuleException;
import org.example.energy.exception.type.ResourceNotFoundException;
import org.example.energy.mapper.ContratoMapper;
import org.example.energy.repository.domain.ClienteRepository;
import org.example.energy.repository.domain.ContratoRepository;
import org.example.energy.service.ContratoService;
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
                        "Cliente ya existe con el id: " + dto.clienteId()
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
    public ContratoResponseDTO update(Integer id, ContratoUpdateDTO dto) {
        return null;
    }

    @Override
    public ContratoResponseDTO cancelar(Integer id) {
        return null;
    }

    @Override
    public ContratoResponseDTO activar(Integer id) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

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
