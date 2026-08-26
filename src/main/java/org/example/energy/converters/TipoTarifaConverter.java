package org.example.energy.converters;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.energy.enums.TipoTarifa;

import java.util.Arrays;

@Converter(autoApply = true)
public class TipoTarifaConverter implements AttributeConverter<TipoTarifa, String> {

    @Override
    public String convertToDatabaseColumn(TipoTarifa attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCodigo();
    }

    @Override
    public TipoTarifa convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Arrays.stream(TipoTarifa.values())
                .filter(t -> t.getCodigo().equalsIgnoreCase(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de tarifa no válido en BBDD: " + dbData));
    }
}
