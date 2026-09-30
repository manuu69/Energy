package org.example.energy.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.energy.auth.dto.LoginRequest;
import org.example.energy.auth.dto.AuthResponse;
import org.example.energy.security.service.JwtService;
import org.example.energy.usuario.entity.Usuario;
import org.example.energy.usuario.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow();

        String token = jwtService.generateToken(usuario);

        return new AuthResponse(token);
    }
}