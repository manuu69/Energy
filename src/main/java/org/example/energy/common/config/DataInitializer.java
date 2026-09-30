package org.example.energy.common.config;

import lombok.RequiredArgsConstructor;
import org.example.energy.common.enums.Role;
import org.example.energy.usuario.entity.Usuario;
import org.example.energy.usuario.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Si no existen usuarios, creamos el ADMIN por defecto
        if (usuarioRepository.count() == 0) {
            Usuario admin = Usuario.builder()
                    .nombre("Administrador Principal")
                    .email("admin@energy.com")
                    .password(passwordEncoder.encode("Admin123!")) // Se guarda como hash BCrypt seguro
                    .role(Role.ADMIN)
                    .build();

            usuarioRepository.save(admin);
            System.out.println("--> Usuario ADMIN inicial creado: admin@energy.com / Admin123!");
        }
    }
}