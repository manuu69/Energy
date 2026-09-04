package org.example.energy.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TipoTarifa {
        TARIFA_2_0_TD,
        TARIFA_3_0_TD,
        TARIFA_3_1_TD,
        TARIFA_6_1_TD
}