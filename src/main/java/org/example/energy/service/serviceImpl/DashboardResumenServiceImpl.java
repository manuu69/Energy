package org.example.energy.service.serviceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.dashboard.DashboardResumenDTO;
import org.example.energy.entity.view.DashboardResumenView;
import org.example.energy.exception.type.ResourceNotFoundException;
import org.example.energy.mapper.DashboardResumenViewMapper;
import org.example.energy.repository.view.DashboardResumenViewRepository;
import org.example.energy.service.DashboardResumenService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class DashboardResumenServiceImpl implements DashboardResumenService {

    private final DashboardResumenViewRepository dashboardRepository;
    private final DashboardResumenViewMapper dashboardMapper;

    /**
     * @return
     */
    @Override
    public DashboardResumenDTO getResumen() {
        DashboardResumenView dashboard = dashboardRepository.getResumen()
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado ningun resumen"));;
        return dashboardMapper.toDTO(dashboard);

    }
}
