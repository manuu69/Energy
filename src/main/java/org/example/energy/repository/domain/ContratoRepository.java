package org.example.energy.repository.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.energy.entity.domain.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ContratoRepository extends JpaRepository<Contrato, Integer> {
}
