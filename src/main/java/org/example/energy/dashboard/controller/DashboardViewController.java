package org.example.energy.dashboard.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energy.dashboard.dto.DashboardResumenDTO;
import org.example.energy.dashboard.service.DashboardResumenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Dashboard")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardViewController {

    private final DashboardResumenService dashboardRService;

    @GetMapping("/resumen")
    public ResponseEntity<DashboardResumenDTO> getResumen(){
        return ResponseEntity.ok().body(dashboardRService.getResumen());
    }
}
