package org.example.energy.cliente.dto;

import org.example.energy.common.enums.TipoCliente;

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
