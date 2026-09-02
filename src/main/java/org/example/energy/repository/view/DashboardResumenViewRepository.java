package org.example.energy.repository.view;

import org.example.energy.entity.view.DashboardResumenView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DashboardResumenViewRepository extends JpaRepository<DashboardResumenView, Integer> {
    @Query("SELECT d FROM DashboardResumenView d")
    Optional<DashboardResumenView> getResumen();
}
