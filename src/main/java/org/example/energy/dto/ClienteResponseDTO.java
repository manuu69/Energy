package org.example.energy.dto;

import org.example.energy.enums.TipoCliente;

import java.time.LocalDate;

public record ClienteResponseDTO(
        Integer clienteId,
        String nombre,
        String email,
        TipoCliente tipo,
        String ciudad,
        LocalDate fechaAlta,
        String segmento
) {
}
