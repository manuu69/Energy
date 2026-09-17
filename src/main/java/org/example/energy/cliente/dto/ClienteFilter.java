package org.example.energy.cliente.dto;

public record ClienteFilter(
        String nombre,
        String ciudad,
        String tipo,
        Boolean eliminado
) {}
