package org.example.energy.service;

import org.example.energy.factura.dto.FacturaCreateDTO;
import org.example.energy.factura.dto.FacturaResponseDTO;
import org.example.energy.contrato.entity.Contrato;
import org.example.energy.factura.entity.Factura;
import org.example.energy.common.enums.EstadoContrato;
import org.example.energy.common.enums.EstadoPago;
import org.example.energy.common.exception.type.BusinessRuleException;
import org.example.energy.common.exception.code.ErrorCode;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.factura.mapper.FacturaMapper;
import org.example.energy.contrato.repository.ContratoRepository;
import org.example.energy.factura.repository.FacturaRepository;
import org.example.energy.factura.service.impl.FacturaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.example.energy.testutil.ContratoTestData.crearContratoActivo;
import static org.example.energy.testutil.ContratoTestData.crearContratoConEstado;
import static org.example.energy.testutil.FacturaTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacturaServiceImplTest {

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private FacturaMapper facturaMapper;

    @InjectMocks
    private FacturaServiceImpl facturaService;

    @Test
    void getAll_whenFacturasExists_thenReturnPage(){
        Pageable pageable = PageRequest.of(0,10);

        Factura factura = crearFactura();
        FacturaResponseDTO dto = crearFacturaResponseDTO();

        Page<Factura> facturaPage = new PageImpl<>(List.of(factura), pageable, 1);

        when(facturaRepository.findAll(pageable)).thenReturn(facturaPage);
        when(facturaMapper.toDTO(factura)).thenReturn(dto);

        Page<FacturaResponseDTO> result = facturaService.getAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().facturaId()).isEqualTo(dto.facturaId());
        assertThat(result.getTotalElements()).isEqualTo(1);

        verify(facturaRepository).findAll(pageable);
        verify(facturaMapper).toDTO(factura);

    }

    @Test
    void getAll_whenFacturasNotExists_thenReturnPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<Factura> facturaPage = Page.empty();

        when(facturaRepository.findAll(pageable)).thenReturn(facturaPage);

        Page<FacturaResponseDTO> result = facturaService.getAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();

        verify(facturaRepository).findAll(pageable);
        verifyNoInteractions(facturaMapper);

    }

    @Test
    void getById_whenFacturaExists_shouldReturnFacturaResponseDTO(){
        Factura factura = new Factura();
        factura.setFacturaId(1);

        FacturaResponseDTO dto = crearFacturaResponseDTO();

        when(facturaRepository.findById(1)).thenReturn(Optional.of(factura));
        when(facturaMapper.toDTO(factura)).thenReturn(dto);

        FacturaResponseDTO result = facturaService.getById(1);

        assertThat(result).isNotNull();
        assertThat(result.facturaId()).isEqualTo(1);

        verify(facturaRepository).findById(1);
        verify(facturaMapper).toDTO(factura);
        verify(facturaRepository, never()).save(any());
    }

    @Test
    void getById_whenFacturaDoesNotExist_shouldThrowResourceNotFoundException(){
        Integer id = 1;
        Factura factura = new Factura();
        factura.setFacturaId(1);

        when(facturaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            facturaService.getById(id);
        });

        verify(facturaRepository, times(1)).findById(id);
        verifyNoInteractions(facturaMapper);
        verify(facturaRepository, never()).save(any());
    }

    @Test
    void createFactura_whenValidData_ShouldReturnFacturaResponseDTO(){
        Integer contratoId = 8;

        FacturaCreateDTO createDTO = crearFacturaCreateDTO();
        Contrato contrato = crearContratoActivo();
        Factura factura = crearFactura();
        FacturaResponseDTO responseDTO = crearFacturaResponseDTO();

        when(contratoRepository.findById(contratoId)).thenReturn(Optional.of(contrato));
        when(facturaMapper.toEntity(createDTO)).thenReturn(factura);
        when(facturaRepository.save(factura)).thenReturn(factura);
        when(facturaMapper.toDTO(factura)).thenReturn(responseDTO);

        FacturaResponseDTO result = facturaService.create(createDTO);

        assertThat(result).isNotNull();
        assertThat(result.contratoId()).isEqualTo(contratoId);
        assertThat(result.estadoPago()).isEqualTo(EstadoPago.PENDIENTE);

        verify(contratoRepository).findById(contratoId);
        verify(facturaMapper).toEntity(createDTO);
        verify(facturaRepository).save(factura);
        verify(facturaMapper).toDTO(factura);
    }

    @Test
    void createFactura_whenContratoDoesntExist_ShouldThrowResourceNotFoundException(){
        Integer contratoId = 8;

        FacturaCreateDTO createDTO = crearFacturaCreateDTO();

        when(contratoRepository.findById(contratoId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->{
            facturaService.create(createDTO);
        });

        verify(contratoRepository).findById(contratoId);
        verifyNoInteractions(facturaMapper);
        verify(facturaRepository, never()).save(any());

    }

    @Test
    void createFactura_whenContratoIsNotActive_ShouldThrowBusinessRuleException(){
        Integer contratoId = 8;

        FacturaCreateDTO createDTO = crearFacturaCreateDTO();
        Contrato contrato = crearContratoConEstado(EstadoContrato.BAJA);

        when(contratoRepository.findById(contratoId)).thenReturn(Optional.of(contrato));

        assertThrows(BusinessRuleException.class, () ->{
            facturaService.create(createDTO);
        });

        verify(contratoRepository).findById(contratoId);
        verifyNoInteractions(facturaMapper);
        verify(facturaRepository, never()).save(any());

    }

    @Test
    void createFactura_whenFacturaAlreadyExistsForThatMonth_ShouldThrowBusinessRuleException(){
        Integer contratoId = 8;

        FacturaCreateDTO createDTO = crearFacturaCreateDTO();
        Contrato contrato = crearContratoConEstado(EstadoContrato.ACTIVO);

        when(contratoRepository.findById(contratoId)).thenReturn(Optional.of(contrato));
        when(facturaRepository.existsByContratoContratoIdAndFechaEmisionBetween(
                eq(contratoId), any(LocalDate.class), any(LocalDate.class)
        )).thenReturn(true);

        assertThrows(BusinessRuleException.class, () ->{
            facturaService.create(createDTO);
        });

        verify(contratoRepository).findById(contratoId);
        verify(facturaRepository).existsByContratoContratoIdAndFechaEmisionBetween(
                eq(contratoId),
                any(LocalDate.class),
                any(LocalDate.class)
        );
        verify(facturaRepository, never()).save(any());

    }

    @Test
    void payFactura_whenFacturaIsPending_ShouldChangeStatusToPagada(){
        Integer id = 1;
        Factura factura = crearFacturaPendiente();
        FacturaResponseDTO dto = crearFacturaResponseDTOConEstado(EstadoPago.PAGADA);

        when(facturaRepository.findById(id)).thenReturn(Optional.of(factura));
        when(facturaMapper.toDTO(factura)).thenReturn(dto);

        FacturaResponseDTO result = facturaService.pagarFactura(id);

        assertThat(result).isNotNull();
        assertThat(result.facturaId()).isEqualTo(id);
        assertThat(result.estadoPago()).isEqualTo(EstadoPago.PAGADA);

        verify(facturaRepository).findById(id);
        verify(facturaMapper).toDTO(factura);
        assertThat(factura.getEstadoPago()).isEqualTo(EstadoPago.PAGADA);
    }

    @Test
    void payFactura_whenFacturaAlreadyPagada_ShouldThrowBusinessRuleException() {
        Integer id = 1;

        Factura factura = crearFacturaPagada();

        when(facturaRepository.findById(id)).thenReturn(Optional.of(factura));

        assertThatThrownBy(() -> facturaService.pagarFactura(id))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.FACTURA_YA_PAGADA.name());

        assertThat(factura.getEstadoPago()).isEqualTo(EstadoPago.PAGADA);

        verify(facturaRepository).findById(id);
        verify(facturaRepository, never()).save(any());
        verifyNoInteractions(facturaMapper);
    }

    @Test
    void payFactura_whenFacturaIsCancelada_shouldThrowBusinessRuleException() {
        Integer id = 1;

        Factura factura = crearFacturaCancelada();

        when(facturaRepository.findById(id)).thenReturn(Optional.of(factura));

        assertThatThrownBy(() -> facturaService.pagarFactura(id))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.BUSINESS_RULE_VIOLATION.name());

        assertThat(factura.getEstadoPago()).isEqualTo(EstadoPago.CANCELADA);

        verify(facturaRepository).findById(id);
        verify(facturaRepository, never()).save(any());
        verifyNoInteractions(facturaMapper);

    }

    @Test
    void payFactura_whenFacturaDoesNotExist_shouldThrowResourceNotFoundException() {
        Integer id = 999;

        when(facturaRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> facturaService.pagarFactura(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Factura no encontrada");

        verify(facturaRepository).findById(id);
        verifyNoInteractions(facturaMapper);
        verify(facturaRepository, never()).save(any());
    }

    @Test
    void cancelarFactura_whenFacturaIsPending_ShouldChangeStatusToCancelada() {
        Integer id = 1;

        Factura factura = crearFacturaPendiente();
        FacturaResponseDTO dto = crearFacturaResponseDTOConEstado(EstadoPago.CANCELADA);

        when(facturaRepository.findById(id)).thenReturn(Optional.of(factura));
        when(facturaMapper.toDTO(factura)).thenReturn(dto);

        FacturaResponseDTO result = facturaService.cancelarFactura(id);

        assertThat(result).isNotNull();
        assertThat(result.estadoPago()).isEqualTo(EstadoPago.CANCELADA);
        assertThat(factura.getEstadoPago()).isEqualTo(EstadoPago.CANCELADA);

        verify(facturaRepository).findById(id);
        verify(facturaMapper).toDTO(factura);
        verify(facturaRepository, never()).save(any());
    }

    @Test
    void cancelarFactura_whenFacturaDoesNotExist_ShouldThrowResourceNotFoundException() {
        Integer id = 99;

        when(facturaRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> facturaService.cancelarFactura(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Factura no encontrada");

        verify(facturaRepository).findById(id);
        verify(facturaRepository, never()).save(any());
        verifyNoInteractions(facturaMapper);
    }

    @Test
    void cancelarFactura_whenFacturaIsPagada_ShouldThrowBusinessRuleException() {
        Integer id = 1;

        Factura factura = crearFacturaPagada();

        when(facturaRepository.findById(id)).thenReturn(Optional.of(factura));

        assertThatThrownBy(() -> facturaService.cancelarFactura(id))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.FACTURA_YA_PAGADA.name());

        assertThat(factura.getEstadoPago()).isEqualTo(EstadoPago.PAGADA);

        verify(facturaRepository).findById(id);
        verify(facturaRepository, never()).save(any());
        verifyNoInteractions(facturaMapper);
    }

    @Test
    void cancelarFactura_whenFacturaAlreadyCancelada_ShouldThrowBusinessRuleException() {
        Integer id = 1;

        Factura factura = crearFacturaCancelada();

        when(facturaRepository.findById(id)).thenReturn(Optional.of(factura));

        assertThatThrownBy(() -> facturaService.cancelarFactura(id))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.BUSINESS_RULE_VIOLATION.name());

        assertThat(factura.getEstadoPago()).isEqualTo(EstadoPago.CANCELADA);

        verify(facturaRepository).findById(id);
        verify(facturaRepository, never()).save(any());
        verifyNoInteractions(facturaMapper);
    }

    @Test
    void delete_whenFacturaExists_shouldDeleteFactura() {
        Integer id = 9;

        Factura factura = new Factura();
        factura.setFacturaId(id);
        factura.setEstadoPago(EstadoPago.PENDIENTE);

        when(facturaRepository.findById(id)).thenReturn(Optional.of(factura));

        facturaService.deleteById(id);

        verify(facturaRepository).findById(id);
        verify(facturaRepository).delete(factura);
    }
    @Test
    void delete_whenFacturaDoesNotExist_shouldThrowResourceNotFoundException() {
        // Arrange
        Integer id = 999;

        when(facturaRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> facturaService.deleteById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Factura no encontrada");

        verify(facturaRepository).findById(id);
        verify(facturaRepository, never()).delete(any());
    }

    @Test
    void delete_whenFacturaIsPagada_shouldThrowBusinessRuleException() {
        // Arrange
        Integer id = 9;

        Factura factura = new Factura();
        factura.setFacturaId(id);
        factura.setEstadoPago(EstadoPago.PAGADA);

        when(facturaRepository.findById(id)).thenReturn(Optional.of(factura));

        // Act & Assert
        assertThatThrownBy(() -> facturaService.deleteById(id))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.FACTURA_YA_PAGADA.name());

        verify(facturaRepository).findById(id);
        verify(facturaRepository, never()).delete(any());
    }

}

