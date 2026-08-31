package org.example.energy.mapper;

import org.example.energy.dto.lectura.LecturaCreateDTO;
import org.example.energy.dto.lectura.LecturaResponseDTO;
import org.example.energy.dto.lectura.LecturaUpdateDTO;
import org.example.energy.entity.domain.Lectura;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LecturaMapper {

    @Mapping(target = "contratoId", source = "contrato.contratoId")
    LecturaResponseDTO toDTO(Lectura lectura);

    @Mapping(target = "lecturaId", ignore = true)
    @Mapping(target = "contrato", ignore = true)
    Lectura toEntity(LecturaCreateDTO dto);

    @Mapping(target = "lecturaId", ignore = true)
    @Mapping(target = "contrato", ignore = true)
    void updateEntityFromDTO(
            LecturaUpdateDTO dto,
            @MappingTarget Lectura lectura
    );
}
