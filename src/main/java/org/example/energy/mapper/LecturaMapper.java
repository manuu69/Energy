package org.example.energy.mapper;

import org.example.energy.dto.lectura.LecturaAnalisisDTO;
import org.example.energy.dto.lectura.LecturaCreateDTO;
import org.example.energy.dto.lectura.LecturaResponseDTO;
import org.example.energy.dto.lectura.LecturaUpdateDTO;
import org.example.energy.entity.domain.Lectura;
import org.example.energy.entity.view.LecturaAnalisis;
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
