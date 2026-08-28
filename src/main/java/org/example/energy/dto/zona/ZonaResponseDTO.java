package org.example.energy.dto.zona;

public record ZonaResponseDTO(
        Integer zonaId,
        String nombre,
        Integer nivel,
        String descripcion
) {
}
