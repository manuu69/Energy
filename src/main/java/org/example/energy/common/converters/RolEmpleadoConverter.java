package org.example.energy.common.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.energy.common.enums.RolEmpleado;

@Converter(autoApply = true)
public class RolEmpleadoConverter implements AttributeConverter<RolEmpleado, String> {

    @Override
    public String convertToDatabaseColumn(RolEmpleado attribute) {
        return attribute == null ? null : attribute.getDescripcionBD();
    }

    @Override
    public RolEmpleado convertToEntityAttribute(String dbData) {
        return dbData == null ? null : RolEmpleado.fromDescripcion(dbData);
    }
}