package org.example.energy.mapper;

import org.example.energy.dto.dashboard.resumen.ResumenFacturacionClienteResponseDTO;
import org.example.energy.entity.view.ResumenFacturacionClienteView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ResumenFacturacionClienteMapper {

    ResumenFacturacionClienteResponseDTO toDTO(
            ResumenFacturacionClienteView resumen
    );
}
