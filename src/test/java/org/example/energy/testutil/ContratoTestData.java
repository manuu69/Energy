package org.example.energy.testutil;

import org.example.energy.contrato.entity.Contrato;
import org.example.energy.common.enums.EstadoContrato;

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
        return contrato;
    }
}
