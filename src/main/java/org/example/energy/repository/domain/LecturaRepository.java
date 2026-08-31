package org.example.energy.repository.domain;

import org.example.energy.dto.lectura.LecturaUpdateDTO;
import org.example.energy.entity.domain.Lectura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LecturaRepository extends JpaRepository<Lectura, Integer> {
}
