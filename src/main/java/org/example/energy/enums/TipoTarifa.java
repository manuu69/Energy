package org.example.energy.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TipoTarifa {

        TARIFA_2_0_TD("2.0TD"),
        TARIFA_3_0_TD("3.0TD"),
        TARIFA_3_1_TD("3.1TD"),
        TARIFA_6_1_TD("6.1TD");

        private final String codigo;

        @JsonValue
        public String getCodigo() {
                return codigo;
        }

        @JsonCreator
        public static TipoTarifa fromCodigo(String codigo) {
                if (codigo == null) {
                        return null;
                }
                return Arrays.stream(values())
                        .filter(tipo -> tipo.codigo.equalsIgnoreCase(codigo))
                        .findFirst()
                        .orElse(null);
        }
}