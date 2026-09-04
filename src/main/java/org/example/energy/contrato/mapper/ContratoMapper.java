package org.example.energy.contrato.mapper;

import org.example.energy.contrato.dto.ContratoCreateDTO;
import org.example.energy.contrato.dto.ContratoResponseDTO;
import org.example.energy.contrato.dto.ContratoUpdateDTO;
import org.example.energy.contrato.entity.Contrato;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContratoMapper {

    @Mapping(target = "clienteId", source = "cliente.clienteId")
    @Mapping(target = "zonaId", source = "zona.zonaId")
    ContratoResponseDTO toDTO(Contrato contrato);

    @Mapping(target = "contratoId", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "zona", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Contrato toEntity(ContratoCreateDTO dto);

    @Mapping(target = "contratoId", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "zona", ignore = true)
    @Mapping(target = "fechaInicio", ignore = true)
    @Mapping(target = "estado", ignore = true)
    void updateEntityFromDTO(
            ContratoUpdateDTO dto,
            @MappingTarget Contrato contrato
    );
}