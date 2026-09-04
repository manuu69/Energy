package org.example.energy.dashboard.repository;

import lombok.AllArgsConstructor;
import org.example.energy.dashboard.dto.DashboardResumenDTO;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class DashboardResumenViewRepository {

    private final JdbcClient jdbcClient;

    public Optional<DashboardResumenDTO> getResumen() {
        String sql = "SELECT * FROM public.vw_dashboard_resumen";

        return jdbcClient.sql(sql)
                .query(DashboardResumenDTO.class)
                .optional();
    }
}
