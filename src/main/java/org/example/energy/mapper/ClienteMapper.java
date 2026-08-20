package org.example.energy.mapper;

import org.example.energy.dto.ClienteCreateDTO;
import org.example.energy.dto.ClienteResponseDTO;
import org.example.energy.dto.ClienteUpdateDTO;
import org.example.energy.entity.domain.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClienteMapper {

    ClienteResponseDTO toDTO(Cliente cliente);

    @Mapping(target = "clienteId", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "fechaEliminacion", ignore = true)
    @Mapping(target = "eliminadoPor", ignore = true)
    Cliente toEntity(ClienteCreateDTO dto);

    @Mapping(target = "clienteId", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "eliminado", ignore = true)
    @Mapping(target = "fechaEliminacion", ignore = true)
    @Mapping(target = "eliminadoPor", ignore = true)
    void updateEntityFromDTO(
            ClienteUpdateDTO dto,
            @MappingTarget Cliente cliente
    );
}