package org.example.energy.common.converters;

import org.example.energy.common.enums.RolEmpleado;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToRolEmpleadoConverter implements Converter<String, RolEmpleado> {

    @Override
    public RolEmpleado convert(String source) {
        return RolEmpleado.fromDescripcion(source);
    }
}