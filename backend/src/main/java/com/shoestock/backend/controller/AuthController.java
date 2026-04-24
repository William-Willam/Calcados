package com.shoestock.backend.controller;

import com.shoestock.backend.dto.AuthDTO;
import com.shoestock.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // POST /api/auth/login
    // Único endpoint público — não precisa de token para acessar
    @PostMapping("/login")
    public ResponseEntity<AuthDTO.Response> login(@Valid @RequestBody AuthDTO.Request dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}