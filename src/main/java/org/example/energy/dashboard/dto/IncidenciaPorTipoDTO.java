package org.example.energy.dashboard.dto;

public record IncidenciaPorTipoDTO(
        String tipo,
        Long total,
        Long abiertas,
        Long enGestion,
        Long cerradas
) {
}
