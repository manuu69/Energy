package org.example.energy.mapper;

import org.example.energy.dto.dashboard.DashboardResumenDTO;
import org.example.energy.entity.view.DashboardResumenView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DashboardResumenViewMapper {
    DashboardResumenDTO toDTO(DashboardResumenView view);
}
