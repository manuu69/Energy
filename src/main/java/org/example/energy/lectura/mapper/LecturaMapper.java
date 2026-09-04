package org.example.energy.lectura.mapper;

import org.example.energy.lectura.dto.LecturaAnalisisDTO;
import org.example.energy.lectura.dto.LecturaCreateDTO;
import org.example.energy.lectura.dto.LecturaResponseDTO;
import org.example.energy.lectura.dto.LecturaUpdateDTO;
import org.example.energy.lectura.entity.Lectura;
import org.example.energy.lectura.entity.LecturaAnalisis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LecturaMapper {

    @Mapping(target = "contratoId", source = "contrato.contratoId")
    @Mapping(target = "fecha", source = "fecha")
    LecturaResponseDTO toDTO(Lectura lectura);


    LecturaAnalisisDTO toAnalisisDTO(LecturaAnalisis lecturaAnalisis);

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
