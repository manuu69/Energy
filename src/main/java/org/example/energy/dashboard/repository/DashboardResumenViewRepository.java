package org.example.energy.dashboard.repository;

import lombok.AllArgsConstructor;
import org.example.energy.common.enums.TipoCliente;
import org.example.energy.dashboard.dto.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Timer;

@Repository
@AllArgsConstructor
public class DashboardResumenViewRepository {

    private final JdbcClient jdbcClient;

    public Optional<DashboardResumenDTO> getResumen() {
        String sql = "SELECT * FROM public.vw_dashboard_resumen";

        return jdbcClient.sql(sql)
                .query(DashboardResumenDTO.class)
                .optional();
    }

    public List<TopDeudorDTO> getTopDeudor(int limit) {
        String sql = "SELECT c.nombre, COALESCE(SUM(f.importe), 2) AS deuda_total\n" +
                "FROM clientes c\n" +
                "JOIN contratos co ON c.cliente_id = co.cliente_id\n" +
                "JOIN facturas f   ON co.contrato_id = f.contrato_id\n" +
                "WHERE f.estado_pago = 'PENDIENTE'\n" +
                "  AND f.fecha_vencimiento < CURRENT_DATE\n" +
                "  AND c.eliminado = FALSE\n" +
                "GROUP BY c.cliente_id, c.nombre\n" +
                "ORDER BY deuda_total DESC\n" +
                "LIMIT :limit;";

        return jdbcClient
                .sql(sql)
                .param("limit", limit)
                .query(TopDeudorDTO.class)
                .list();
    }

    public Optional<FacturacionMensualDTO> getFacturacionMensual(int mes) {
        String sql = "SELECT\n" +
                "                DATE_TRUNC('month', fecha_emision) AS mes,\n" +
                "                SUM(importe) AS total_facturado,\n" +
                "                SUM(importe) FILTER (WHERE estado_pago = 'PAGADA') AS total_cobrado\n" +
                "            FROM facturas\n" +
                "            WHERE fecha_emision >= DATE_TRUNC('month', CURRENT_DATE) - (:mes || ' months')::interval\n" +
                "            GROUP BY DATE_TRUNC('month', fecha_emision)\n" +
                "            ORDER BY mes desc" +
                "               limit 1";

        return jdbcClient
                .sql(sql)
                .param("mes", mes)
                .query(FacturacionMensualDTO.class)
                .optional();
    }

    public Long countClienteByTipo(final TipoCliente tipo){
        String sql = "select count(*) from clientes c where c.tipo = :tipo;";

        return jdbcClient.sql(sql)
                .param("tipo", tipo.name())
                .query(Long.class)
                .single();
    }

    public List<IncidenciaPorTipoDTO> getByTipoIncidencia(){
        String sql = "SELECT\n" +
                "                tipo,\n" +
                "                COUNT(*)                                              AS total,\n" +
                "                COUNT(*) FILTER (WHERE estado = 'ABIERTA')           AS abiertas,\n" +
                "                COUNT(*) FILTER (WHERE estado = 'EN_GESTION')        AS en_gestion,\n" +
                "                COUNT(*) FILTER (WHERE estado = 'CERRADA')           AS cerradas\n" +
                "            FROM incidencias\n" +
                "            GROUP BY tipo\n" +
                "            ORDER BY total desc;";

        return jdbcClient.sql(sql)
                .query(IncidenciaPorTipoDTO.class)
                .list();
    }

    public List<ConsumoPorZonaDTO> getConsumoPorZona(){
        String sql = "SELECT\n" +
                "                z.nombre                    AS zona,\n" +
                "                z.nivel,\n" +
                "                COALESCE(SUM(l.consumo_kwh), 0) AS consumo_total,\n" +
                "                COUNT(DISTINCT co.contrato_id)  AS num_contratos\n" +
                "            FROM zonas z\n" +
                "            LEFT JOIN contratos co ON co.zona_id = z.zona_id\n" +
                "                AND co.estado = 'ACTIVO'\n" +
                "            LEFT JOIN lecturas l ON l.contrato_id = co.contrato_id\n" +
                "                AND l.fecha >= DATE_TRUNC('month', CURRENT_DATE)\n" +
                "            WHERE z.nivel = 4\n" +
                "            GROUP BY z.zona_id, z.nombre, z.nivel\n" +
                "            ORDER BY consumo_total DESC";

        return jdbcClient.sql(sql)
                .query(ConsumoPorZonaDTO.class)
                .list();
    }
}
