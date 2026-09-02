package org.example.energy.service.serviceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dto.dashboard.DashboradResumenDTO;
import org.example.energy.mapper.DashboardResumenViewMapper;
import org.example.energy.repository.view.DashboardResumenViewRepository;
import org.example.energy.service.DashboardResumenService;
import org.springframework.stereotype.Service;

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
    public DashboradResumenDTO getResumen() {
        return null;
    }
}
