package org.example.energy.mapper;

import org.example.energy.dto.incidencia.IncidenciaCreateDTO;
import org.example.energy.dto.incidencia.IncidenciaResponseDTO;
import org.example.energy.dto.incidencia.IncidenciaUpdateDTO;
import org.example.energy.entity.domain.Incidencia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IncidenciaMapper {

    @Mapping(
            target = "contratoId",
            source = "contrato.contratoId"
    )
    IncidenciaResponseDTO toDTO(Incidencia incidencia);

    List<IncidenciaResponseDTO> toDTOList(
            List<Incidencia> incidencias
    );

    @Mapping(target = "incidenciaId", ignore = true)
    @Mapping(target = "contrato", ignore = true)
    @Mapping(target = "fechaApertura", ignore = true)
    @Mapping(target = "fechaCierre", ignore = true)
    @Mapping(target = "estado", ignore = true)
    Incidencia toEntity(IncidenciaCreateDTO dto);

    @Mapping(target = "incidenciaId", ignore = true)
    @Mapping(target = "contrato", ignore = true)
    @Mapping(target = "fechaApertura", ignore = true)
    @Mapping(target = "fechaCierre", ignore = true)
    @Mapping(target = "estado", ignore = true)
    void updateEntityFromDTO(
            IncidenciaUpdateDTO dto,
            @MappingTarget Incidencia incidencia
    );
}
