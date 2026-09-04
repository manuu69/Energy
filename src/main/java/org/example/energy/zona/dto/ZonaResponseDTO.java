package org.example.energy.zona.dto;

public record ZonaResponseDTO(
        Integer zonaId,
        String nombre,
        Integer nivel,
        String descripcion
) {
}
