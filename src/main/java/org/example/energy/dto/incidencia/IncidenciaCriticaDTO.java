package org.example.energy.dto.incidencia;

public record IncidenciaCriticaDTO(
        Integer incidenciaId,
        Integer contratoId,
        String tipo,
        String nombreCliente,
        Integer diasAbierta
) {}
