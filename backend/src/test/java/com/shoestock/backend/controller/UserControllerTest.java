package com.shoestock.backend.controller;

import com.shoestock.backend.entity.Role;
import com.shoestock.backend.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("Gestor deve cadastrar estoquista com sucesso")
    void gestorDeveCadastrarEstoquista() throws Exception {
        mockMvc.perform(post("/api/users")
                        .header("Authorization", gestorToken) // envia o token do gestor
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(buildUserRequest(
                                "Ana Estoquista",
                                "ana@test.com",
                                "123456",
                                Role.ESTOQUISTA
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Ana Estoquista"))
                .andExpect(jsonPath("$.role").value("ESTOQUISTA"));
    }

    @Test
    @DisplayName("Estoquista não deve acessar rota de usuários")
    void estoquistaNaoDeveAcessarRotaDeUsuarios() throws Exception {
        mockMvc.perform(post("/api/users")
                        .header("Authorization", estoquistaToken) // token do estoquista
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(buildUserRequest(
                                "Teste",
                                "teste@test.com",
                                "123456",
                                Role.ESTOQUISTA
                        ))))
                .andExpect(status().isForbidden()); // 403 — sem permissão
    }

    @Test
    @DisplayName("Acesso sem token deve retornar 403")
    void semTokenDeveRetornar403() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(buildUserRequest(
                                "Teste",
                                "teste@test.com",
                                "123456",
                                Role.GESTOR
                        ))))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Gestor deve listar estoquistas")
    void gestorDeveListarEstoquistas() throws Exception {
        // Cria um estoquista primeiro
        userService.create(buildUserRequest(
                "Ana Estoquista",
                "ana@test.com",
                "123456",
                Role.ESTOQUISTA
        ));

        mockMvc.perform(get("/api/users/estoquistas")
                        .header("Authorization", gestorToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].role").value("ESTOQUISTA"));
    }

    @Test
    @DisplayName("Gestor deve deletar usuário com sucesso")
    void gestorDeveDeletarUsuario() throws Exception {
        // Cria um usuário para deletar
        var created = userService.create(buildUserRequest(
                "Para Deletar",
                "deletar@test.com",
                "123456",
                Role.ESTOQUISTA
        ));

        mockMvc.perform(delete("/api/users/" + created.getId())
                        .header("Authorization", gestorToken))
                .andExpect(status().isNoContent()); // 204
    }
}