package com.shoestock.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

// @Configuration diz ao Spring que essa classe contém configurações
// @EnableWebSecurity ativa o controle de segurança da aplicação
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // @Bean registra esse objeto no Spring para ser injetado onde precisar
    // BCrypt é o algoritmo de criptografia mais usado para senhas
    // Ele gera um hash diferente a cada vez, mesmo para a mesma senha
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuração temporária: libera todos os endpoints por enquanto
    // Vamos proteger as rotas com JWT na etapa de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desativa CSRF (desnecessário em APIs REST)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permite todas as requisições por enquanto
                );

        return http.build();
    }
}