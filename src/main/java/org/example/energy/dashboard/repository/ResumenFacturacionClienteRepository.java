package org.example.energy.dashboard.repository;

import lombok.AllArgsConstructor;
import org.example.energy.dashboard.dto.ResumenFacturacionClienteResponseDTO;
import org.example.energy.dashboard.entity.ResumenFacturacionClienteView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ResumenFacturacionClienteRepository {

    private final JdbcClient jdbcClient;

    public Page<ResumenFacturacionClienteResponseDTO> findAll(Pageable pageable) {
        String sql = """
            SELECT * FROM vw_resumen_facturacion_cliente
            LIMIT :limit OFFSET :offset
            """;

        List<ResumenFacturacionClienteResponseDTO> contenido = jdbcClient.sql(sql)
                .param("limit", pageable.getPageSize())
                .param("offset", pageable.getOffset())
                .query(ResumenFacturacionClienteResponseDTO.class)
                .list();

        String countSql = "SELECT COUNT(*) FROM vw_resumen_facturacion_cliente";
        Long total = jdbcClient.sql(countSql)
                .query(Long.class)
                .single();

        return new PageImpl<>(contenido, pageable, total);
    }
    public Optional<ResumenFacturacionClienteResponseDTO> findById(Integer clienteId) {
        String sql = "SELECT * FROM vw_resumen_facturacion_cliente WHERE cliente_id = :clienteId";

        return jdbcClient.sql(sql)
                .param("clienteId", clienteId)
                .query(ResumenFacturacionClienteResponseDTO.class)
                .optional();
    }
}
