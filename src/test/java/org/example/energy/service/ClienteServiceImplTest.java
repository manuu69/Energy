package org.example.energy.service;

import org.example.energy.cliente.dto.ClienteCreateDTO;
import org.example.energy.cliente.dto.ClienteResponseDTO;
import org.example.energy.cliente.dto.ClienteUpdateDTO;
import org.example.energy.cliente.entity.Cliente;
import org.example.energy.cliente.mapper.ClienteMapper;
import org.example.energy.cliente.repository.ClienteRepository;
import org.example.energy.cliente.service.ClienteServiceImpl;
import org.example.energy.common.enums.EstadoContrato;
import org.example.energy.common.exception.type.BusinessRuleException;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.contrato.dto.ContratoResponseDTO;
import org.example.energy.contrato.dto.ContratoUpdateDTO;
import org.example.energy.contrato.entity.Contrato;
import org.example.energy.contrato.mapper.ContratoMapper;
import org.example.energy.contrato.repository.ContratoRepository;
import org.example.energy.contrato.service.ContratoServiceImpl;
import org.example.energy.testUtil.ClienteTestData;
import org.example.energy.testUtil.ContratoTestData;
import org.example.energy.zona.entity.Zona;
import org.example.energy.zona.repository.ZonaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.energy.testUtil.ClienteTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private ZonaRepository zonaRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @Mock
    private ContratoMapper contratoMapper;

    @InjectMocks
    private ContratoServiceImpl contratoService;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void getAll_ReturnsPage(){
        Pageable pageable = PageRequest.of(0,10);

        Cliente cliente = crearCliente();
        ClienteResponseDTO dto = crearClienteResponseDTO();

        Page<Cliente> page = new PageImpl<>(List.of(cliente));

        when(clienteRepository.findAll(pageable)).thenReturn(page);
        when(clienteMapper.toDTO(cliente)).thenReturn(dto);

        Page<ClienteResponseDTO> result = clienteService.getAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().clienteId()).isEqualTo(dto.clienteId());
        assertThat(result.getTotalElements()).isEqualTo(1);

        verify(clienteRepository).findAll(pageable);
        verify(clienteMapper).toDTO(cliente);
    }

    @Test
    void getAll_WhenNoClientes_ReturnsEmptyPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<Cliente> page = new PageImpl<>(List.of());

        when(clienteRepository.findAll(pageable)).thenReturn(page);

        Page<ClienteResponseDTO> result = clienteService.getAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();

        verify(clienteRepository).findAll(pageable);
        verifyNoInteractions(clienteMapper);
    }

    @Test
    void getById_WhenClienteExists_ReturnClienteResponseDTO(){
        Cliente cliente = crearCliente();
        ClienteResponseDTO dto = crearClienteResponseDTO();

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDTO(cliente)).thenReturn(dto);

        ClienteResponseDTO result = clienteService.getById(1);

        assertThat(result).isNotNull();
        assertThat(result.clienteId()).isEqualTo(1);

        verify(clienteRepository).findById(1);
        verify(clienteMapper).toDTO(cliente);
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void getById_WhenClienteNotExists_ThrowResourceNotFoundException(){
        when(clienteRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.getById(1);
        });

        verify(clienteRepository, times(1)).findById(1);
        verifyNoInteractions(clienteMapper);
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void createCliente_whenValidData_ShouldReturnClienteResponseDTO(){
        ClienteCreateDTO createDTO = crearClienteCreateDTO();
        Cliente cliente = crearCliente();
        ClienteResponseDTO responseDTO = crearClienteResponseDTO();

        when(clienteMapper.toEntity(createDTO)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toDTO(cliente)).thenReturn(responseDTO);

        ClienteResponseDTO result = clienteService.create(createDTO);

        assertThat(result).isNotNull();
        assertThat(result.clienteId()).isEqualTo(1);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void createCliente_whenEmailDoesNotExist_shouldCreateCliente() {
        ClienteCreateDTO createDTO = crearClienteCreateDTO();
        Cliente cliente = crearCliente();
        Cliente clienteGuardado = crearCliente();
        ClienteResponseDTO responseDTO = crearClienteResponseDTO();

        when(clienteRepository.existsByEmail(createDTO.email()))
                .thenReturn(false);

        when(clienteMapper.toEntity(createDTO))
                .thenReturn(cliente);

        when(clienteRepository.save(cliente))
                .thenReturn(clienteGuardado);

        when(clienteMapper.toDTO(clienteGuardado))
                .thenReturn(responseDTO);

        ClienteResponseDTO resultado = clienteService.create(createDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.clienteId())
                .isEqualTo(responseDTO.clienteId());

        assertThat(cliente.isEliminado()).isFalse();
        assertThat(cliente.getFechaEliminacion()).isNull();
        assertThat(cliente.getEliminadoPor()).isNull();

        verify(clienteRepository)
                .existsByEmail(createDTO.email());

        verify(clienteMapper).toEntity(createDTO);
        verify(clienteRepository).save(cliente);
        verify(clienteMapper).toDTO(clienteGuardado);
    }

    @Test
    void createCliente_whenEmailAlreadyExists_shouldThrowBusinessRuleException() {
        ClienteCreateDTO createDTO = crearClienteCreateDTO();

        when(clienteRepository.existsByEmail(createDTO.email()))
                .thenReturn(true);

        assertThatThrownBy(() -> clienteService.create(createDTO))
                .isInstanceOf(BusinessRuleException.class);

        verify(clienteRepository)
                .existsByEmail(createDTO.email());

        verifyNoInteractions(clienteMapper);
        verify(clienteRepository, never()).save(any());
    }


    @Test
    void update_whenContratoExists_updatesAllowedFieldsAndReturnsDTO() {
        Integer contratoId = 8;

        Contrato contrato = ContratoTestData.crearContratoActivo();
        ContratoUpdateDTO updateDTO =
                ContratoTestData.crearContratoUpdateDTO();

        /*Zona nuevaZona = new Zona(1, "HOLA", 3, "JEJE", );*/
        ContratoResponseDTO responseDTO =
                ContratoTestData.crearContratoResponseDTO();

        when(contratoRepository.findById(contratoId))
                .thenReturn(Optional.of(contrato));

        /*when(zonaRepository.findById(updateDTO.zonaId()))
                .thenReturn(Optional.of(nuevaZona));*/

        when(contratoMapper.toDTO(contrato))
                .thenReturn(responseDTO);

        ContratoResponseDTO resultado =
                contratoService.update(contratoId, updateDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.contratoId()).isEqualTo(contratoId);

        verify(contratoRepository).findById(contratoId);
        //verify(zonaRepository).findById(updateDTO.zonaId());
        verify(contratoMapper).updateEntityFromDTO(updateDTO, contrato);
        verify(contratoMapper).toDTO(contrato);

        verify(contratoRepository, never()).save(any());
    }

    @Test
    void update_whenContratoDoesNotExist_throwsResourceNotFoundException() {
        Integer contratoId = 999;

        ContratoUpdateDTO updateDTO =
                ContratoTestData.crearContratoUpdateDTO();

        when(contratoRepository.findById(contratoId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> contratoService.update(contratoId, updateDTO)
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(contratoId));

        verify(contratoRepository).findById(contratoId);
        verifyNoInteractions(contratoMapper);
        verify(contratoRepository, never()).save(any());
    }

    @Test
    void update_whenClienteDoesNotExist_throwsResourceNotFoundException() {
        Integer clienteId = 999;

        ClienteUpdateDTO updateDTO =
                ClienteTestData.crearClienteUpdateDTO();

        when(clienteRepository.findById(clienteId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> clienteService.update(clienteId, updateDTO)
        )
                .isInstanceOf(ResourceNotFoundException.class);

        verify(clienteRepository).findById(clienteId);
        verifyNoInteractions(clienteMapper);
        verify(clienteRepository, never()).findByEmail(any());
    }

    @Test
    void darBaja_whenClienteExists_shouldDarBajaCliente() {
        Integer clienteId = 1;

        when(clienteRepository.existsById(clienteId))
                .thenReturn(true);

        clienteService.darBaja(clienteId);

        verify(clienteRepository).existsById(clienteId);
        verify(clienteRepository).darDeBajaCliente(clienteId);
    }

    @Test
    void darBaja_whenClienteDoesNotExist_shouldThrowResourceNotFoundException() {
        Integer clienteId = 999;

        when(clienteRepository.existsById(clienteId))
                .thenReturn(false);

        assertThatThrownBy(() -> clienteService.darBaja(clienteId))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(clienteRepository).existsById(clienteId);

        verify(clienteRepository, never())
                .darDeBajaCliente(anyInt());
    }

}
