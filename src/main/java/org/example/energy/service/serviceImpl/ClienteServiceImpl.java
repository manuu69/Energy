package org.example.energy.service.serviceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.cliente.ClienteCreateDTO;
import org.example.energy.dto.cliente.ClienteResponseDTO;
import org.example.energy.dto.cliente.ClienteUpdateDTO;
import org.example.energy.entity.domain.Cliente;
import org.example.energy.enums.Segmento;
import org.example.energy.enums.TipoCliente;
import org.example.energy.exception.type.BusinessRuleException;
import org.example.energy.exception.code.ErrorCode;
import org.example.energy.exception.type.ResourceNotFoundException;
import org.example.energy.mapper.ClienteMapper;
import org.example.energy.repository.domain.ClienteRepository;
import org.example.energy.service.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Slf4j
public class ClienteServiceImpl implements ClienteService {


    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> getAll(Pageable pageable) {
        log.debug(
                "Consultando clientes paginados. page={}, size={}, sort={}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<Cliente> clientes = clienteRepository.findAll(pageable);

        log.info(
                "Consulta de clientes realizada. totalElements={}, totalPages={}, currentPage={}",
                clientes.getTotalElements(),
                clientes.getTotalPages(),
                clientes.getNumber()
        );
        return clientes.map(clienteMapper::toDTO);

    }

    @Override
    public ClienteResponseDTO getById(Integer id) {
        log.debug("Buscando cliente con id={}", id);

        Cliente cliente = findById(id);
        return clienteMapper.toDTO(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO getByEmail(String email) {
        log.debug("Buscando cliente con email={}", email);
        return clienteMapper.toDTO(clienteRepository.findByEmail(email).orElseThrow(
                () -> {
                    log.warn("Cliente no encontrado con email={}", email);

                    return new ResourceNotFoundException(
                            "Cliente no encontrado con email: " + email
                    );
                }
        ));
    }

    @Override
    public Page<ClienteResponseDTO> getByCiudad(String ciudad, Pageable pageable) {
        log.debug(
                "Consultando clientes por ciudad. ciudad={}, page={}, size={}, sort={}",
                ciudad,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<Cliente> clientes = clienteRepository.findByCiudad(ciudad, pageable);

        log.info(
                "Consulta de clientes por ciudad realizada. ciudad={}, totalElements={}, totalPages={}, currentPage={}",
                ciudad,
                clientes.getTotalElements(),
                clientes.getTotalPages(),
                clientes.getNumber()
        );
        return clientes.map(clienteMapper::toDTO);
    }

    @Override
    public Page<ClienteResponseDTO> getBySegmento(Segmento segmento, Pageable pageable) {
        log.debug(
                "Consultando clientes por segmento. ciudad={}, page={}, size={}, sort={}",
                segmento,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<Cliente> clientes = clienteRepository.findBySegmento(segmento, pageable);

        log.info(
                "Consulta de clientes por segemento realizada. segmento={}, totalElements={}, totalPages={}, currentPage={}",
                segmento,
                clientes.getTotalElements(),
                clientes.getTotalPages(),
                clientes.getNumber()
        );
        return clientes.map(clienteMapper::toDTO);
    }

    @Override
    public Page<ClienteResponseDTO> getByTipo(TipoCliente tipo, Pageable pageable) {
        log.debug(
                "Consultando clientes por tipo. tipo={}, page={}, size={}, sort={}",
                tipo,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );

        Page<Cliente> clientes = clienteRepository.findByTipo(tipo, pageable);

        log.info(
                "Consulta de clientes por tipo realizada. tipo={}, totalElements={}, totalPages={}, currentPage={}",
                tipo,
                clientes.getTotalElements(),
                clientes.getTotalPages(),
                clientes.getNumber()
        );
        return clientes.map(clienteMapper::toDTO);
    }

    @Override
    @Transactional
    public ClienteResponseDTO create(ClienteCreateDTO dto) {
        if (clienteRepository.existsByEmail(dto.email())){
            log.warn(
                    "Creación rechazada. Ya existe un cliente activo con email={}",
                    dto.email()
            );
            throw new BusinessRuleException(
                    ErrorCode.DATABASE_CONFLICT
            );
        }
        Cliente cliente = clienteMapper.toEntity(dto);
        cliente.setEliminado(false);
        cliente.setFechaEliminacion(null);
        cliente.setFechaAlta(LocalDate.now());

        Cliente savedCliente = clienteRepository.save(cliente);

        log.info(
                "Cliente creado correctamente. clienteId={}, email={}",
                savedCliente.getClienteId(),
                savedCliente.getEmail()
        );
        return clienteMapper.toDTO(savedCliente);

    }

    @Override
    @Transactional
    public ClienteResponseDTO update(ClienteUpdateDTO dto, Integer id) {
        Cliente cliente = findById(id);

        clienteRepository.findByEmail(dto.email())
                        .filter(found -> !found.getClienteId().equals(id))
                                .ifPresent(foundCliente ->{
                                    log.warn(
                                            "Actualización rechazada. Email duplicado. email={}, clienteIdExistente={}",
                                            dto.email(),
                                            foundCliente.getClienteId()
                                    );

                                    throw new BusinessRuleException(ErrorCode.DATABASE_CONFLICT);
                                });

        clienteMapper.updateEntityFromDTO(dto, cliente);
        log.info("Cliente actualizado correctamente con el id {}", cliente.getClienteId());

        return clienteMapper.toDTO(cliente);
    }

    @Override
    @Transactional
    public void darBaja(Integer id) {
        if (!clienteRepository.existsById(id)){
            log.warn("Cliente no existe con id={}", id);
            throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND.name());
        }

        clienteRepository.darDeBajaCliente(id);
        log.info("Cliente con el id{} dado de baja correctamente", id);

    }

    private Cliente findById(Integer id){
        return clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cliente no encontrado con id={}", id);

                    return new ResourceNotFoundException(
                            "Cliente no encontrado con el ID: " + id
                    );
                });
    }
}
