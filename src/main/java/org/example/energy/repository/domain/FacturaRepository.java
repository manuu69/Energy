package org.example.energy.repository.domain;

import org.example.energy.entity.domain.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    List<Factura> findByContratoContratoId(Integer contratoId);
}
