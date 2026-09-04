package org.example.energy.cliente.mapper;

import org.example.energy.cliente.entity.Cliente;
import org.example.energy.cliente.dto.ClienteCreateDTO;
import org.example.energy.cliente.dto.ClienteResponseDTO;
import org.example.energy.cliente.dto.ClienteUpdateDTO;
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