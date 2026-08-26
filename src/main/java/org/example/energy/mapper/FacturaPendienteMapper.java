package org.example.energy.mapper;

import org.example.energy.dto.factura.FacturaResponseDTO;
import org.example.energy.entity.view.FacturaPendienteView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FacturaPendienteMapper {
    FacturaResponseDTO toDTO(FacturaPendienteView view);
}
