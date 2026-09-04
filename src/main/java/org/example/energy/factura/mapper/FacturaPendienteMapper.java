package org.example.energy.factura.mapper;

import org.example.energy.factura.dto.FacturaResponseDTO;
import org.example.energy.factura.entity.FacturaPendienteView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FacturaPendienteMapper {
    FacturaResponseDTO toDTO(FacturaPendienteView view);
}
