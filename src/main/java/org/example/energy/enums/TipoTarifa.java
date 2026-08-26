package org.example.energy.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoTarifa {

        TARIFA_2_0_TD("2.0TD"),
        TARIFA_3_0_TD("3.0TD"),
        TARIFA_3_1_TD("3.1TD"),
        TARIFA_6_1_TD("6.1TD");

        @JsonValue
        private final String codigo;



}
