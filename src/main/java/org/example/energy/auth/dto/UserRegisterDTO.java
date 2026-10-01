package org.example.energy.auth.dto;

import jakarta.validation.constraints.NotNull;

public record UserRegisterDTO(
        @NotNull(message = "El nombre es obligatorio")
        String nombre,
        @NotNull(message = "El email es obligatorio")
        String email,
        @NotNull(message = "La contraseña es obligatorio")
        String password
) {
}
