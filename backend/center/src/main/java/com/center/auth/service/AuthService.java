package com.center.auth.service;

import com.center.auth.dto.AuthRequest;
import com.center.auth.dto.AuthResponse;
import com.center.auth.dto.QuickRegisterRequest;
import com.center.auth.dto.RegisterRequestDTO;
import com.center.auth.model.User;
import com.center.auth.model.UserRole;
import com.center.auth.repository.UserRepository;
import com.center.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    // Registro completo (post-creación de datos)
    public AuthResponse register(RegisterRequestDTO request) {
        // Se espera que RegisterRequestDTO contenga: name, email, password, etc.
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER) // Asignar rol por defecto
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    /**
     * Registro rápido: crea un usuario usando solo email, password y el checklist de habeas data.
     * Se asigna por defecto el rol USER y el nombre se deja vacío para ser completado luego.
     */
    public AuthResponse quickRegister(QuickRegisterRequest request) {
        // Validar que se haya aceptado la política de habeas data
        if (!request.isAcceptedHabeasData()) {
            throw new RuntimeException("Debe aceptar la política de habeas data");
        }

        // Crear el usuario con los datos mínimos (nombre vacío para completarlo después)
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name("") // Nombre vacío por ahora
                .role(UserRole.USER) // Rol por defecto
                .build();

        userRepository.save(user);

        // Generar token JWT usando el email del usuario
        String token = jwtService.generateToken(user.getEmail());

        // Retornar la respuesta con AuthResponse (usando el builder de AuthResponse)
        return AuthResponse.builder()
                .token(token)
                .name(user.getName()) // Será "" inicialmente
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    // Método de login para usuarios ya registrados
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
