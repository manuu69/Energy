package org.example.energy.mapper;

import org.example.energy.dto.zona.ZonaCreateDTO;
import org.example.energy.dto.zona.ZonaResponseDTO;
import org.example.energy.dto.zona.ZonaUpdateDTO;
import org.example.energy.entity.domain.Zona;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
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
