package org.example.energy.empleado.mapper;

import org.example.energy.empleado.dto.EmpleadoCreateDTO;
import org.example.energy.empleado.dto.EmpleadoResponseDTO;
import org.example.energy.empleado.dto.EmpleadoUpdateDTO;
import org.example.energy.empleado.entity.Empleado;
import org.example.energy.factura.dto.FacturaResponseDTO;
import org.example.energy.factura.entity.Factura;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmpleadoMapper {

    @Mapping(target = "jefeId", source = "jefe.empleadoId")
    EmpleadoResponseDTO toDTO(Empleado empleado);

    List<EmpleadoResponseDTO> toDTOList(List<Empleado> empleados);

    Empleado toEntity(EmpleadoCreateDTO createDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "empleadoId", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "jefe", ignore = true)
    void updateEntityFromDto(EmpleadoUpdateDTO updateDTO, @MappingTarget Empleado empleado);
}
