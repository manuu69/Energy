package org.example.energy.factura.mapper;

import org.example.energy.factura.dto.FacturaCreateDTO;
import org.example.energy.factura.dto.FacturaResponseDTO;
import org.example.energy.factura.entity.Factura;
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
    @Mapping(target = "contrato", ignore = true)
    Factura toEntity(FacturaCreateDTO dto);
}
