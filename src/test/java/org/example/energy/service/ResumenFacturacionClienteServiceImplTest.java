package org.example.energy.service;

import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.dashboard.dto.ResumenFacturacionClienteResponseDTO;
import org.example.energy.dashboard.repository.ResumenFacturacionClienteRepository;
import org.example.energy.dashboard.service.impl.ResumenFacturacionClienteServiceImpl;
import org.example.energy.testUtil.ResumenFacturacionClienteTestData;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumenFacturacionClienteServiceImplTest {

    @Mock
    private ResumenFacturacionClienteRepository resumenRepository;

    @InjectMocks
    private ResumenFacturacionClienteServiceImpl resumenFacturacionClienteService;

    @Test
    void getAll_DebeRetornarPaginaDeResumenes() {
        Pageable pageable = PageRequest.of(0, 10);
        ResumenFacturacionClienteResponseDTO dto = ResumenFacturacionClienteTestData.crearResumenFacturacionClienteResponseDTO();
        Page<ResumenFacturacionClienteResponseDTO> page = new PageImpl<>(List.of(dto));

        when(resumenRepository.findAll(pageable)).thenReturn(page);

        Page<ResumenFacturacionClienteResponseDTO> result = resumenFacturacionClienteService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Empresa Ejemplo S.A.", result.getContent().get(0).nombre());
        verify(resumenRepository).findAll(pageable);
    }

    @Test
    void getByClienteId_CuandoExiste_DebeRetornarResumenDTO() {
        Integer clienteId = 1;
        ResumenFacturacionClienteResponseDTO dto = ResumenFacturacionClienteTestData.crearResumenFacturacionClienteResponseDTO();

        when(resumenRepository.findById(clienteId)).thenReturn(Optional.of(dto));

        ResumenFacturacionClienteResponseDTO result = resumenFacturacionClienteService.getByClienteId(clienteId);

        assertNotNull(result);
        assertEquals(clienteId, result.clienteId());
        assertEquals(new BigDecimal("12500.50"), result.totalFacturado());
        verify(resumenRepository).findById(clienteId);
    }

    @Test
    void getByClienteId_CuandoNoExiste_DebeLanzarResourceNotFoundException() {
        Integer clienteId = 99;

        when(resumenRepository.findById(clienteId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> resumenFacturacionClienteService.getByClienteId(clienteId));

        verify(resumenRepository).findById(clienteId);
    }
}