package org.example.energy.testUtil;

import org.example.energy.common.enums.TipoLectura;
import org.example.energy.contrato.entity.Contrato;
import org.example.energy.lectura.dto.LecturaAnalisisDTO;
import org.example.energy.lectura.dto.LecturaCreateDTO;
import org.example.energy.lectura.dto.LecturaResponseDTO;
import org.example.energy.lectura.dto.LecturaUpdateDTO;
import org.example.energy.lectura.entity.Lectura;
import org.example.energy.lectura.entity.LecturaAnalisis;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class LecturaTestData {

    private LecturaTestData() {
    }

    public static Lectura crearLectura() {
        Contrato contrato = new Contrato();
        contrato.setContratoId(10);

        Lectura lectura = new Lectura();
        lectura.setLecturaId(1);
        lectura.setContrato(contrato);
        lectura.setFecha(LocalDate.of(2026, 3, 1));
        lectura.setConsumoKwh(new BigDecimal("150.50"));
        lectura.setTipoLectura(TipoLectura.REAL);
        return lectura;
    }

    public static LecturaCreateDTO crearLecturaCreateDTO() {
        return new LecturaCreateDTO(
                10,
                LocalDate.of(2026, 3, 1),
                new BigDecimal("150.50"),
                TipoLectura.REAL
        );
    }

    public static LecturaResponseDTO crearLecturaResponseDTO() {
        return new LecturaResponseDTO(
                1,
                10,
                LocalDate.of(2026, 3, 1),
                new BigDecimal("150.50"),
                TipoLectura.REAL
        );
    }

    public static LecturaUpdateDTO crearLecturaUpdateDTO() {
        return new LecturaUpdateDTO(
                LocalDate.of(2026, 3, 2),
                new BigDecimal("200.00"),
                TipoLectura.ESTIMADA
        );
    }

    /*public static LecturaAnalisis crearLecturaAnalisis() {
        LecturaAnalisis analisis = new LecturaAnalisis();
        analisis.setLecturaId(1);
        analisis.setContratoId(10);
        analisis.setFecha(LocalDate.of(2026, 3, 1));
        analisis.setConsumoKwh(new BigDecimal("150.50"));
        analisis.setDiferenciaConMedia(new BigDecimal("10.50"));
        return analisis;
    }*/

    public static LecturaAnalisisDTO crearLecturaAnalisisDTO() {
        return new LecturaAnalisisDTO(
                1,
                10,
                LocalDate.of(2026, 3, 1),
                new BigDecimal("150.50"),
                new BigDecimal("10.50")
        );
    }
}