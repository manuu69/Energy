package org.example.energy.lectura.repository;

import org.example.energy.lectura.entity.UltimaLecturaView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UltimaLecturaViewRepository
        extends JpaRepository<UltimaLecturaView, Long> {
}
