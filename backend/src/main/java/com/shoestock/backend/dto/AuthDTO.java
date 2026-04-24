package com.shoestock.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthDTO {

    // O que o cliente envia para fazer login
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @Email(message = "Email inválido")
        @NotBlank(message = "Email é obrigatório")
        private String email;

        @NotBlank(message = "Senha é obrigatória")
        private String password;
    }

    // O que a API devolve após login bem sucedido
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String token;   // o JWT gerado
        private String name;    // nome do usuário
        private String email;
        private String role;    // perfil: GESTOR, ESTOQUISTA ou CLIENTE
    }
}