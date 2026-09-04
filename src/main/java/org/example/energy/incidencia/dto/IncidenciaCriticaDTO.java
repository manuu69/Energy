package org.example.energy.incidencia.dto;

public record IncidenciaCriticaDTO(
        Integer incidenciaId,
        Integer contratoId,
        String tipo,
        String nombreCliente,
        Integer diasAbierta
) {}
