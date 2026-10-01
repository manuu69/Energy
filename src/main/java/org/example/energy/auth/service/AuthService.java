package org.example.energy.auth.service;

import org.example.energy.auth.dto.LoginRequest;
import org.example.energy.auth.dto.AuthResponse;
import org.example.energy.auth.dto.UserRegisterDTO;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(UserRegisterDTO dto);
}
