package org.example.energy.factura.repository;

import org.example.energy.factura.entity.FacturaPendienteView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaPendienteViewRepository
        extends JpaRepository<FacturaPendienteView, Long> {
}
