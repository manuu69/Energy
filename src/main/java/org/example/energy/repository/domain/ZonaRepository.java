package org.example.energy.repository.domain;

import org.example.energy.entity.domain.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, Integer> {
}
