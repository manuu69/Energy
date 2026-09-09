package org.example.energy.service;

import org.example.energy.cliente.entity.Cliente;
import org.example.energy.cliente.repository.ClienteRepository;
import org.example.energy.common.enums.EstadoContrato;
import org.example.energy.common.enums.TipoTarifa;
import org.example.energy.common.exception.type.BusinessRuleException;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.contrato.dto.ContratoCreateDTO;
import org.example.energy.contrato.dto.ContratoResponseDTO;
import org.example.energy.contrato.dto.ContratoUpdateDTO;
import org.example.energy.contrato.entity.Contrato;
import org.example.energy.contrato.mapper.ContratoMapper;
import org.example.energy.contrato.repository.ContratoRepository;
import org.example.energy.contrato.service.ContratoServiceImpl;
import org.example.energy.testUtil.ContratoTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.energy.testUtil.ContratoTestData.crearContratoResponseDTO;
import static org.example.energy.testUtil.ContratoTestData.crearContratoUpdateDTO;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContratoServiceImplTest {

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ContratoMapper contratoMapper;

    @InjectMocks
    private ContratoServiceImpl contratoService;

    @Test
    void getAll_ReturnsPage(){
        Pageable pageable = PageRequest.of(0,10);

        Contrato contrato = ContratoTestData.crearContratoConEstado(EstadoContrato.ACTIVO);
        ContratoResponseDTO dto = crearContratoResponseDTO();
        Page<Contrato> page = new PageImpl<>(List.of(contrato));

        when(contratoRepository.findAll(pageable)).thenReturn(page);
        when(contratoMapper.toDTO(contrato)).thenReturn(dto);

        Page<ContratoResponseDTO> result = contratoService.getAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().contratoId()).isEqualTo(dto.contratoId());
        assertThat(result.getTotalElements()).isEqualTo(1);

        verify(contratoRepository).findAll(pageable);
        verify(contratoMapper).toDTO(contrato);
    }

    @Test
    void getAll_WhenNoContratos_ReturnsEmptyPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<Contrato> page = new PageImpl<>(List.of());

        when(contratoRepository.findAll(pageable)).thenReturn(page);

        Page<ContratoResponseDTO> result = contratoService.getAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();

        verify(contratoRepository).findAll(pageable);
        verifyNoInteractions(contratoMapper);
    }

    @Test
    void getById_WhenContratoExists_ReturnContratoResponseDTO(){
        Contrato contrato = ContratoTestData.crearContratoConEstado(EstadoContrato.ACTIVO);
        ContratoResponseDTO dto = crearContratoResponseDTO();

        when(contratoRepository.findById(8)).thenReturn(Optional.of(contrato));
        when(contratoMapper.toDTO(contrato)).thenReturn(dto);

        ContratoResponseDTO result = contratoService.getById(8);

        assertThat(result).isNotNull();
        assertThat(result.contratoId()).isEqualTo(8);

        verify(contratoRepository).findById(8);
        verify(contratoMapper).toDTO(contrato);
        verify(contratoRepository, never()).save(any());
    }

    @Test
    void getById_WhenContratoNotExists_ThrowsResourceNotFoundException(){
        when(contratoRepository.findById(8)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            contratoService.getById(8);
        });

        verify(contratoRepository, times(1)).findById(8);
        verifyNoInteractions(contratoMapper);
        verify(contratoRepository, never()).save(any());
    }



    @Test
    void createContrato_whenValidData_ShouldReturnContratoResponseDTO(){
        ContratoCreateDTO createDTO = ContratoTestData.crearContratoCreateDTO();
        Contrato contrato = ContratoTestData.crearContratoActivo();
        ContratoResponseDTO responseDTO = crearContratoResponseDTO();
        Cliente cliente = new Cliente();
        cliente.setClienteId(1);

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(contratoMapper.toEntity(createDTO)).thenReturn(contrato);
        when(contratoRepository.save(contrato)).thenReturn(contrato);
        when(contratoMapper.toDTO(contrato)).thenReturn(responseDTO);

        ContratoResponseDTO resultado = contratoService.create(createDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.contratoId()).isEqualTo(8);
        verify(clienteRepository).findById(1);
        verify(contratoRepository).save(contrato);
    }

    @Test
    void createContrato_whenClienteNotExists_ShouldThrowResourceNotFoundException(){
        ContratoCreateDTO createDTO = ContratoTestData.crearContratoCreateDTO();

        when(clienteRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contratoService.create(createDTO))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(contratoRepository, never()).save(any());
    }


    @Test
    void update_whenContratoExists_returnsDTO() {
        Integer id = 8;

        Contrato contrato = ContratoTestData.crearContratoConEstado(EstadoContrato.ACTIVO);
        ContratoUpdateDTO updateDTO = crearContratoUpdateDTO();
        ContratoResponseDTO responseDTO = crearContratoResponseDTO();

        when(contratoRepository.findById(id))
                .thenReturn(Optional.of(contrato));

        when(contratoMapper.toDTO(contrato))
                .thenReturn(responseDTO);

        ContratoResponseDTO result = contratoService.update(id, updateDTO);

        assertThat(result).isNotNull();
        assertThat(result.contratoId()).isEqualTo(id);

        verify(contratoRepository).findById(id);
        verify(contratoMapper).updateEntityFromDTO(updateDTO, contrato);
        verify(contratoMapper).toDTO(contrato);

        verify(contratoRepository, never()).save(any());
    }

    @Test
    void update_whenContratoNotExists_throwsResourceNotFoundException() {
        ContratoUpdateDTO updateDTO = crearContratoUpdateDTO();

        when(contratoRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contratoService.update(999, updateDTO))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(contratoRepository, never()).save(any());
    }

    @Test
    void darBaja_whenContratoActivo_cambiaEstado() {
        Integer id = 8;

        Contrato contrato = ContratoTestData.crearContratoActivo();

        ContratoResponseDTO responseDTO =
                ContratoTestData.crearContratoBajaResponseDTO();

        when(contratoRepository.findById(id))
                .thenReturn(Optional.of(contrato));

        when(contratoMapper.toDTO(contrato))
                .thenReturn(responseDTO);

        ContratoResponseDTO resultado = contratoService.darBaja(id);

        assertThat(resultado).isNotNull();
        assertThat(resultado.estado()).isEqualTo(EstadoContrato.BAJA);

        assertThat(contrato.getEstado()).isEqualTo(EstadoContrato.BAJA);

        verify(contratoRepository).findById(id);
        verify(contratoMapper).toDTO(contrato);
        verify(contratoRepository, never()).save(any());
    }

    @Test
    void darBaja_whenContratoYaEnBaja_throwsBusinessRuleException() {
        Contrato contrato = ContratoTestData.crearContratoConEstado(EstadoContrato.BAJA);

        when(contratoRepository.findById(8)).thenReturn(Optional.of(contrato));

        assertThatThrownBy(() -> contratoService.darBaja(8))
                .isInstanceOf(BusinessRuleException.class);

        verify(contratoRepository, never()).save(any());
    }


    @Test
    void suspender_whenContratoActivo_cambiaEstado() {
        Contrato contrato = ContratoTestData.crearContratoActivo();

        when(contratoRepository.findById(8)).thenReturn(Optional.of(contrato));
        //when(contratoRepository.save(contrato)).thenReturn(contrato);
        when(contratoMapper.toDTO(contrato)).thenReturn(
                new ContratoResponseDTO(8, 1, 1, TipoTarifa.TARIFA_2_0_TD,
                        BigDecimal.valueOf(4.6), LocalDate.of(2024, 1, 1),
                        EstadoContrato.SUSPENDIDO)
        );

        ContratoResponseDTO resultado = contratoService.suspender(8);

        assertThat(resultado.estado()).isEqualTo(EstadoContrato.SUSPENDIDO);
        verify(contratoMapper).toDTO(contrato);
        verify(contratoRepository, never()).save(any());
    }

    @Test
    void suspender_whenContratoEnBaja_throwsBusinessRuleException() {
        Contrato contrato = ContratoTestData.crearContratoConEstado(EstadoContrato.BAJA);

        when(contratoRepository.findById(8)).thenReturn(Optional.of(contrato));

        assertThatThrownBy(() -> contratoService.suspender(8))
                .isInstanceOf(BusinessRuleException.class);

        verify(contratoRepository, never()).save(any());
    }

    @Test
    void activar_whenContratoSuspendido_cambiaEstado() {
        Contrato contrato = ContratoTestData.crearContratoConEstado(EstadoContrato.SUSPENDIDO);

        when(contratoRepository.findById(8)).thenReturn(Optional.of(contrato));
        //when(contratoRepository.save(contrato)).thenReturn(contrato);
        when(contratoMapper.toDTO(contrato)).thenReturn(
                new ContratoResponseDTO(8, 1, 1, TipoTarifa.TARIFA_2_0_TD,
                        BigDecimal.valueOf(4.6), LocalDate.of(2024, 1, 1),
                        EstadoContrato.ACTIVO)
        );

        ContratoResponseDTO resultado = contratoService.activar(8);

        assertThat(resultado.estado()).isEqualTo(EstadoContrato.ACTIVO);
        verify(contratoRepository, never()).save(any());
    }

    @Test
    void activar_whenContratoEnBaja_throwsBusinessRuleException() {
        Contrato contrato = ContratoTestData.crearContratoConEstado(EstadoContrato.BAJA);

        when(contratoRepository.findById(8)).thenReturn(Optional.of(contrato));

        assertThatThrownBy(() -> contratoService.activar(8))
                .isInstanceOf(BusinessRuleException.class);

        verify(contratoRepository, never()).save(any());
    }


    @Test
    void deleteById_whenContratoExists_deletesContrato() {
        when(contratoRepository.existsById(8)).thenReturn(true);

        contratoService.deleteById(8);

        verify(contratoRepository).deleteById(8);
    }

    @Test
    void deleteById_whenContratoNotExists_throwsResourceNotFoundException() {
        when(contratoRepository.existsById(999)).thenReturn(false);

        assertThatThrownBy(() -> contratoService.deleteById(999))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(contratoRepository, never()).deleteById(any());
    }
}
