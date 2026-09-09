package org.example.energy.empleado.mapper;

import org.example.energy.empleado.dto.EmpleadoCreateDTO;
import org.example.energy.empleado.dto.EmpleadoResponseDTO;
import org.example.energy.empleado.entity.Empleado;
import org.example.energy.factura.dto.FacturaResponseDTO;
import org.example.energy.factura.entity.Factura;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmpleadoMapper {

    EmpleadoResponseDTO toDTO(Empleado empleado);

    List<EmpleadoResponseDTO> toDTOList(List<Empleado> empleados);

    Empleado toEntity(EmpleadoCreateDTO createDTO);
}
