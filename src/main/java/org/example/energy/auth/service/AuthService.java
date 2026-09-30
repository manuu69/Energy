package org.example.energy.auth.service;

import org.example.energy.auth.dto.LoginRequest;
import org.example.energy.auth.dto.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}
