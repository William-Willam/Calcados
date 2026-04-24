package com.shoestock.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                // STATELESS = sem sessão. Cada requisição precisa do token
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // ✅ Rotas públicas — qualquer um acessa
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

                        // 🔒 Somente GESTOR
                        .requestMatchers("/api/users/**").hasRole("GESTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("GESTOR")

                        // 🔒 GESTOR e ESTOQUISTA
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasAnyRole("GESTOR", "ESTOQUISTA")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAnyRole("GESTOR", "ESTOQUISTA")

                        // 🔒 Somente CLIENTE
                        .requestMatchers("/api/cart/**").hasRole("CLIENTE")

                        // Qualquer outra rota precisa estar autenticado
                        .anyRequest().authenticated()
                )

                // Adiciona o filtro JWT antes do filtro padrão do Spring
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}