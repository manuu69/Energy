package org.example.energy.testUtil;

import org.example.energy.common.enums.TipoTarifa;
import org.example.energy.contrato.dto.ContratoCreateDTO;
import org.example.energy.contrato.dto.ContratoResponseDTO;
import org.example.energy.contrato.dto.ContratoUpdateDTO;
import org.example.energy.contrato.entity.Contrato;
import org.example.energy.common.enums.EstadoContrato;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContratoTestData {

    public static Contrato crearContratoActivo() {
        return crearContratoConEstado(EstadoContrato.ACTIVO);
    }

    public static Contrato crearContratoConEstado(EstadoContrato estado) {
        Contrato contrato = new Contrato();
        contrato.setContratoId(8);
        contrato.setEstado(estado);
        contrato.setFechaInicio(LocalDate.of(2024, 1, 1));
        contrato.setTarifa(TipoTarifa.TARIFA_2_0_TD);
        contrato.setPotenciaKw(BigDecimal.valueOf(4.6));
        return contrato;
    }

    public static ContratoResponseDTO crearContratoResponseDTO() {
        return new ContratoResponseDTO(
                8,
                1,
                1,
                TipoTarifa.TARIFA_2_0_TD,
                BigDecimal.valueOf(4.6),
                LocalDate.of(2024, 1, 1),
                EstadoContrato.ACTIVO
        );
    }

    public static ContratoResponseDTO crearContratoBajaResponseDTO() {
        return new ContratoResponseDTO(
                8,
                1,
                1,
                TipoTarifa.TARIFA_2_0_TD,
                BigDecimal.valueOf(4.6),
                LocalDate.of(2024, 1, 1),
                EstadoContrato.BAJA
        );
    }

    public static ContratoCreateDTO crearContratoCreateDTO() {
        return new ContratoCreateDTO(
                1,
                TipoTarifa.TARIFA_2_0_TD,
                BigDecimal.valueOf(4.6),
                LocalDate.of(2024, 1, 1),
                1
        );
    }

    public static ContratoUpdateDTO crearContratoUpdateDTO() {
        return new ContratoUpdateDTO(
                1,
                TipoTarifa.TARIFA_3_0_TD,
                BigDecimal.valueOf(15.0)
        );
    }
}
