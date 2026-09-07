package org.example.energy.historico_tarifa.repository;

import lombok.RequiredArgsConstructor;
import org.example.energy.historico_tarifa.dto.HistoricoTarifaResponseDTO;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HistoricoTarifaRepository {

    private final JdbcClient jdbcClient;

    public List<HistoricoTarifaResponseDTO> findByContratoId(final Integer contratoId){
        String sql = """
            SELECT historico_id, contrato_id, tarifa, precio_kwh, potencia_kw as potenciaKw, fecha_inicio, fecha_fin, motivo_cambio
            FROM historico_tarifas
            WHERE contrato_id = :contratoId
            ORDER BY fecha_inicio DESC
            """;

        return jdbcClient.sql(sql)
                .param("contratoId", contratoId)
                .query(HistoricoTarifaResponseDTO.class)
                .list();
    }
}
