package org.example.energy.dashboard.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dashboard.dto.DashboardResumenDTO;
import org.example.energy.common.exception.type.ResourceNotFoundException;
import org.example.energy.dashboard.repository.DashboardResumenViewRepository;
import org.example.energy.dashboard.service.DashboardResumenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class DashboardResumenServiceImpl implements DashboardResumenService {

    private final DashboardResumenViewRepository dashboardRepository;
    //private final DashboardResumenViewMapper dashboardMapper;

    /**
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public DashboardResumenDTO getResumen() {
        return dashboardRepository.getResumen()
                .orElseThrow(() -> new ResourceNotFoundException("No hay nigun dashboard"));
    }
}
