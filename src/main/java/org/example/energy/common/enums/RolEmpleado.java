package org.example.energy.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum RolEmpleado {

    DIRECTOR_GENERAL("Director General"),
    DIRECTOR_OPERACIONES("Dir. Operaciones"),
    DIRECTOR_TECNOLOGIA("Dir. Tecnología"),
    DIRECTOR_COMERCIAL("Dir. Comercial"),
    DIRECTOR_FINANZAS("Dir. Finanzas"),
    JEFE_EQUIPO_GRANDES_CUENTAS("Jefe Equipo Grandes Cuentas"),
    JEFE_EQUIPO_RESIDENCIAL("Jefe Equipo Residencial"),
    JEFE_MANTENIMIENTO("Jefe Mantenimiento"),
    JEFE_RED_DISTRIBUCION("Jefe Red Distribución"),
    JEFE_DESARROLLO("Jefe Desarrollo"),
    JEFE_INFRAESTRUCTURA("Jefe Infraestructura"),
    JEFE_CONTABILIDAD("Jefe Contabilidad"),
    GESTOR_GRANDES_CUENTAS("Gestor Grandes Cuentas"),
    AGENTE_COMERCIAL("Agente Comercial"),
    TECNICO_MANTENIMIENTO("Técnico Mantenimiento"),
    TECNICO_RED("Técnico Red"),
    DESARROLLADOR_SENIOR("Desarrollador Senior"),
    DESARROLLADOR_JUNIOR("Desarrollador Junior"),
    DBA("DBA"),
    CONTABLE("Contable");

    private final String descripcionBD;

    RolEmpleado(String descripcionBD) {
        this.descripcionBD = descripcionBD;
    }

    @JsonValue
    public String getDescripcionBD() {
        return descripcionBD;
    }

    @JsonCreator
    public static RolEmpleado fromDescripcion(String valor) {
        if (valor == null) return null;
        return Arrays.stream(values())
                .filter(r -> r.descripcionBD.equalsIgnoreCase(valor.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Rol no reconocido: " + valor));
    }
}