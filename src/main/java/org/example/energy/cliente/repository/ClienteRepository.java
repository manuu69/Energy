package org.example.energy.cliente.repository;

import org.example.energy.cliente.entity.Cliente;
import org.example.energy.common.enums.Segmento;
import org.example.energy.common.enums.TipoCliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Cliente> findByCiudad(String ciudad, Pageable pageable);
    Page<Cliente> findBySegmento(Segmento segmento, Pageable pageable);
    Page<Cliente> findByTipo(TipoCliente tipo, Pageable pageable);

    boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM public.get_clientes_riesgo()", nativeQuery = true)
    List<Cliente> findClientesEnRiesgo();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "CALL public.dar_baja_cliente(:id)", nativeQuery = true)
    void darDeBajaCliente(@Param("id") Integer id);
}
