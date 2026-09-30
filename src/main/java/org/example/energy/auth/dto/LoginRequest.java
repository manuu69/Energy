package org.example.energy.auth.dto;

public record LoginRequest(
        String email,
        String password
) {
}
