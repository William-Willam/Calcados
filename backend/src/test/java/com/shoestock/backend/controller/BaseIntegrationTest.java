package com.shoestock.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoestock.backend.dto.AuthDTO;
import com.shoestock.backend.dto.UserDTO;
import com.shoestock.backend.entity.Role;
import com.shoestock.backend.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

// @SpringBootTest sobe o contexto completo da aplicação
// @AutoConfigureMockMvc configura o MockMvc automaticamente
// @Transactional garante que cada teste reverte as mudanças no banco ao final
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc; // simula requisições HTTP

    @Autowired
    protected ObjectMapper objectMapper; // converte objetos para JSON e vice-versa

    @Autowired
    protected JwtService jwtService;

    // Tokens prontos para usar nos testes
    protected String gestorToken;
    protected String estoquistaToken;
    protected String clienteToken;

    @BeforeEach
    void setUpTokens() {
        // Gera tokens JWT diretamente sem precisar fazer login
        gestorToken = "Bearer " + jwtService.generateToken("gestor@test.com", "GESTOR");
        estoquistaToken = "Bearer " + jwtService.generateToken("estoquista@test.com", "ESTOQUISTA");
        clienteToken = "Bearer " + jwtService.generateToken("cliente@test.com", "CLIENTE");
    }

    // Método auxiliar que converte qualquer objeto para JSON
    protected String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    // Monta um UserDTO.Request pronto para usar nos testes
    protected UserDTO.Request buildUserRequest(String name, String email, String password, Role role) {
        return UserDTO.Request.builder()
                .name(name)
                .email(email)
                .password(password)
                .role(role)
                .build();
    }
}