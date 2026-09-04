package org.example.energy.dashboard.mapper;

import org.example.energy.dashboard.dto.ResumenFacturacionClienteResponseDTO;
import org.example.energy.dashboard.entity.ResumenFacturacionClienteView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResumenFacturacionClienteMapper {

    ResumenFacturacionClienteResponseDTO toDTO(
            ResumenFacturacionClienteView resumen
    );
}
