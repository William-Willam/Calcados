package com.shoestock.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // @Value injeta o valor do application.properties
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Gera a chave criptográfica a partir da string secreta
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Gera um token JWT para o usuário
    // O token contém: email (subject), role, data de criação e data de expiração
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)               // quem é o dono do token
                .claim("role", role)          // qual o perfil do usuário
                .issuedAt(new Date())         // quando foi gerado
                .expiration(new Date(System.currentTimeMillis() + expiration)) // quando expira
                .signWith(getSigningKey())     // assina com a chave secreta
                .compact();                   // gera a string final do token
    }

    // Extrai o email do token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // Extrai o perfil (role) do token
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    // Verifica se o token é válido e não está expirado
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().after(new Date()); // verifica se não expirou
        } catch (Exception e) {
            return false; // token inválido ou expirado
        }
    }

    // Decodifica o token e retorna os dados internos (claims)
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // usa a chave para verificar a assinatura
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}