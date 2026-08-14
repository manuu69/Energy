package org.example.energy.mapper;

import org.example.energy.dto.FacturaCreateDTO;
import org.example.energy.dto.FacturaResponseDTO;
import org.example.energy.entity.domain.Factura;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FacturaMapper {

    @Mapping(target = "contratoId", source = "contrato.contratoId")
    FacturaResponseDTO toDTO(Factura factura);

    List<FacturaResponseDTO> toDTOList(List<Factura> facturas);

    @Mapping(target = "facturaId", ignore = true)
    @Mapping(target = "contrato.contratoId", source = "contratoId")
    Factura toEntity(FacturaCreateDTO dto);
}
