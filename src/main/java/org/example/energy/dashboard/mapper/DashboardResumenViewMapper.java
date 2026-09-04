package org.example.energy.dashboard.mapper;

import org.example.energy.dashboard.dto.DashboardResumenDTO;
import org.example.energy.dashboard.entity.DashboardResumenView;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DashboardResumenViewMapper {
    DashboardResumenDTO toDTO(DashboardResumenView view);
}
