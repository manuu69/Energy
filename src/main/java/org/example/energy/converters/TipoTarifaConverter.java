package org.example.energy.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.energy.enums.TipoTarifa;

@Converter(autoApply = true)
public class TipoTarifaConverter
        implements AttributeConverter<TipoTarifa, String> {

    @Override
    public String convertToDatabaseColumn(TipoTarifa attribute) {
        return attribute != null
                ? attribute.getCodigo()
                : null;
    }

    @Override
    public TipoTarifa convertToEntityAttribute(String dbData) {
        return dbData != null
                ? TipoTarifa.fromCodigo(dbData)
                : null;
    }
}