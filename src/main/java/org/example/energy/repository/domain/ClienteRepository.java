package org.example.energy.repository.domain;

import org.example.energy.entity.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByEmail(String email);

    @Query(value = "SELECT * FROM public.get_clientes_riesgo()", nativeQuery = true)
    List<Cliente> findClientesEnRiesgo();

    @Modifying
    @Query(value = "CALL dar_baja_cliente(:id)", nativeQuery = true)
    void darDeBajaCliente(@Param("id") Integer id);
}
