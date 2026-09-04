package org.example.energy.contrato.repository;

import org.example.energy.contrato.entity.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ContratoRepository extends JpaRepository<Contrato, Integer> {
    boolean existsById(Integer id);

    @Query(value = "select count(*) from contratos c where c.cliente_id = :clienteId",
            nativeQuery = true)
    long countByClienteId(@Param("clienteId") Integer clienteId);
}
