package com.shoestock.backend.controller;

import com.shoestock.backend.dto.AuthDTO;
import com.shoestock.backend.entity.Role;
import com.shoestock.backend.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Deve fazer login com sucesso e retornar token")
    void deveFazerLoginComSucesso() throws Exception {
        // Cria o usuário no banco de teste
        userService.create(buildUserRequest(
                "Carlos Gestor",
                "carlos@test.com",
                "123456",
                Role.GESTOR
        ));

        // Monta o JSON de login
        AuthDTO.Request loginRequest = AuthDTO.Request.builder()
                .email("carlos@test.com")
                .password("123456")
                .build();

        // Simula POST /api/auth/login e verifica a resposta
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(loginRequest)))
                // Verifica o status HTTP
                .andExpect(status().isOk())
                // Verifica que o campo token existe e não está vazio
                .andExpect(jsonPath("$.token").isNotEmpty())
                // Verifica os outros campos
                .andExpect(jsonPath("$.email").value("carlos@test.com"))
                .andExpect(jsonPath("$.role").value("GESTOR"));
    }

    @Test
    @DisplayName("Deve retornar erro com senha incorreta")
    void deveRetornarErroComSenhaIncorreta() throws Exception {
        userService.create(buildUserRequest(
                "Carlos Gestor",
                "carlos@test.com",
                "123456",
                Role.GESTOR
        ));

        AuthDTO.Request loginRequest = AuthDTO.Request.builder()
                .email("carlos@test.com")
                .password("senhaErrada")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email ou senha inválidos"));
    }

    @Test
    @DisplayName("Deve retornar erro com email inexistente")
    void deveRetornarErroComEmailInexistente() throws Exception {
        AuthDTO.Request loginRequest = AuthDTO.Request.builder()
                .email("naoexiste@test.com")
                .password("123456")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email ou senha inválidos"));
    }
}