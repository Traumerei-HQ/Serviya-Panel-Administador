package com.center.auth.controller;

import com.center.auth.dto.AuthRequest;
import com.center.auth.dto.AuthResponse;
import com.center.auth.dto.QuickRegisterRequest;
import com.center.auth.dto.RegisterRequestDTO;
import com.center.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequestDTO request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

     /**
     * Endpoint para el registro rápido.
     * Recibe un objeto JSON con email, password y el checkbox de habeas data.
     */
    @PostMapping("/quick-register")
    public ResponseEntity<AuthResponse> quickRegister(@RequestBody QuickRegisterRequest request) {
        AuthResponse response = authService.quickRegister(request);
        return ResponseEntity.ok(response);
    }

}
