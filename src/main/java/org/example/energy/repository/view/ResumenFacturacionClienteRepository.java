package org.example.energy.repository.view;

import org.example.energy.entity.view.ResumenFacturacionClienteView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ResumenFacturacionClienteRepository extends Repository<ResumenFacturacionClienteView, Integer> {
    Page<ResumenFacturacionClienteView> findAll(Pageable pageable);
    Optional<ResumenFacturacionClienteView> findById(Integer clienteId);
}
