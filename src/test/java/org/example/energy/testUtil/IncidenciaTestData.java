package org.example.energy.testUtil;

import org.example.energy.common.enums.EstadoIncidencia;
import org.example.energy.common.enums.TipoIncidencia;
import org.example.energy.contrato.entity.Contrato;
import org.example.energy.incidencia.dto.IncidenciaCreateDTO;
import org.example.energy.incidencia.dto.IncidenciaCriticaDTO;
import org.example.energy.incidencia.dto.IncidenciaResponseDTO;
import org.example.energy.incidencia.dto.IncidenciaUpdateDTO;
import org.example.energy.incidencia.entity.Incidencia;

import java.time.LocalDate;

public final class IncidenciaTestData {

    private IncidenciaTestData() {
    }

    public static Incidencia crearIncidencia() {
        Contrato contrato = new Contrato();
        contrato.setContratoId(10);

        Incidencia incidencia = new Incidencia();
        incidencia.setIncidenciaId(1);
        incidencia.setContrato(contrato);
        incidencia.setTipo(TipoIncidencia.AVERIA);
        incidencia.setFechaApertura(LocalDate.now().minusDays(5));
        incidencia.setEstado(EstadoIncidencia.ABIERTA);
        return incidencia;
    }

    public static Incidencia crearIncidenciaEnGestion() {
        Incidencia incidencia = crearIncidencia();
        incidencia.setEstado(EstadoIncidencia.EN_GESTION);
        return incidencia;
    }

    public static Incidencia crearIncidenciaCerrada() {
        Incidencia incidencia = crearIncidencia();
        incidencia.setEstado(EstadoIncidencia.CERRADA);
        incidencia.setFechaCierre(LocalDate.now());
        return incidencia;
    }

    public static IncidenciaCreateDTO crearIncidenciaCreateDTO() {
        return new IncidenciaCreateDTO(
                10,
                TipoIncidencia.AVERIA
        );
    }

    public static IncidenciaResponseDTO crearIncidenciaResponseDTO() {
        return new IncidenciaResponseDTO(
                1,
                10,
                TipoIncidencia.AVERIA,
                LocalDate.now().minusDays(5),
                null,
                EstadoIncidencia.ABIERTA
        );
    }

    public static IncidenciaUpdateDTO crearIncidenciaUpdateDTO() {
        return new IncidenciaUpdateDTO(
                TipoIncidencia.AVERIA
        );
    }

    /*public static IncidenciaCritica crearIncidenciaCritica() {
        IncidenciaCritica critica = new IncidenciaCritica();
        critica.setIncidenciaId(1);
        critica.setContratoId(10);
        critica.setTipo("AVERIA");
        critica.setNombreCliente("Juan Pérez");
        critica.setDiasAbierta(12);
        return critica;
    }*/

    public static IncidenciaCriticaDTO crearIncidenciaCriticaDTO() {
        return new IncidenciaCriticaDTO(
                1,
                10,
                "AVERIA",
                "Juan Pérez",
                12
        );
    }

    public static IncidenciaResponseDTO crearIncidenciaConEstadoDTO(EstadoIncidencia estado) {
        return new IncidenciaResponseDTO(
                1,
                10,
                TipoIncidencia.AVERIA,
                LocalDate.now().minusDays(5),
                null,
                estado
        );
    }
}