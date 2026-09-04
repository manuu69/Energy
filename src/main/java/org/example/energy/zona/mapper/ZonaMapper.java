package org.example.energy.zona.mapper;

import org.example.energy.zona.dto.ZonaCreateDTO;
import org.example.energy.zona.dto.ZonaResponseDTO;
import org.example.energy.zona.dto.ZonaUpdateDTO;
import org.example.energy.zona.entity.Zona;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ZonaMapper {

    ZonaResponseDTO toResponseDTO(Zona zona);

    @Mapping(target = "zonaId", ignore = true)
    Zona toEntity(ZonaCreateDTO dto);

    @Mapping(target = "zonaId", ignore = true)
    void updateEntityFromDTO(
            ZonaUpdateDTO dto,
            @MappingTarget Zona zona
    );
}
