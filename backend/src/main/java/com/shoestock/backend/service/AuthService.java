package com.shoestock.backend.service;

import com.shoestock.backend.dto.AuthDTO;
import com.shoestock.backend.entity.User;
import com.shoestock.backend.repository.UserRepository;
import com.shoestock.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthDTO.Response login(AuthDTO.Request dto) {
        // Busca o usuário pelo email
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));

        // Compara a senha enviada com o hash salvo no banco
        // passwordEncoder.matches() faz a comparação com BCrypt
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        // Gera o token JWT com email e role do usuário
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return AuthDTO.Response.builder()
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}