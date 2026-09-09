package org.example.energy.service;

import org.example.energy.historico_tarifa.dto.HistoricoTarifaResponseDTO;
import org.example.energy.historico_tarifa.repository.HistoricoTarifaRepository;
import org.example.energy.historico_tarifa.service.HistoricoTarifaServiceImpl;
import org.example.energy.testUtil.HistoricoTarifaTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoricoTarifaServiceImplTest {

    @Mock
    private HistoricoTarifaRepository historicoTarifaRepository;

    @InjectMocks
    private HistoricoTarifaServiceImpl historicoTarifaService;

    @Test
    void getByContratoId_CuandoExistenRegistros_DebeRetornarListaDeHistoricos() {
        Integer contratoId = 10;
        HistoricoTarifaResponseDTO responseDTO = HistoricoTarifaTestData.crearHistoricoTarifaResponseDTO();

        when(historicoTarifaRepository.findByContratoId(contratoId)).thenReturn(List.of(responseDTO));

        List<HistoricoTarifaResponseDTO> result = historicoTarifaService.getByContratoId(contratoId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).historicoId());
        assertEquals(contratoId, result.get(0).contratoId());
        assertEquals("Revisión anual de precios", result.get(0).motivoCambio());
        verify(historicoTarifaRepository).findByContratoId(contratoId);
    }

    @Test
    void getByContratoId_CuandoNoExistenRegistros_DebeRetornarListaVacia() {
        Integer contratoId = 99;

        when(historicoTarifaRepository.findByContratoId(contratoId)).thenReturn(Collections.emptyList());

        List<HistoricoTarifaResponseDTO> result = historicoTarifaService.getByContratoId(contratoId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(historicoTarifaRepository).findByContratoId(contratoId);
    }
}