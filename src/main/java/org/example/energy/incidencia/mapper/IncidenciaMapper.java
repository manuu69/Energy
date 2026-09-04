package org.example.energy.incidencia.mapper;

import org.example.energy.incidencia.dto.IncidenciaCreateDTO;
import org.example.energy.incidencia.dto.IncidenciaCriticaDTO;
import org.example.energy.incidencia.dto.IncidenciaResponseDTO;
import org.example.energy.incidencia.dto.IncidenciaUpdateDTO;
import org.example.energy.incidencia.entity.Incidencia;
import org.example.energy.incidencia.entity.IncidenciaCriticaView;
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

    IncidenciaCriticaDTO toCriticaDTO(IncidenciaCriticaView incidenciaCritica);

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
